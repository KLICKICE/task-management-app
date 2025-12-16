package mate.academy.taskmanagementapp.service.attachment;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mate.academy.taskmanagementapp.dto.attachment.AttachmentDto;
import mate.academy.taskmanagementapp.dto.attachment.CreateAttachmentRequestDto;
import mate.academy.taskmanagementapp.exception.DropboxException;
import mate.academy.taskmanagementapp.exception.EntityNotFoundException;
import mate.academy.taskmanagementapp.mapper.AttachmentMapper;
import mate.academy.taskmanagementapp.model.attachment.Attachment;
import mate.academy.taskmanagementapp.model.task.Task;
import mate.academy.taskmanagementapp.repository.AttachmentRepository;
import mate.academy.taskmanagementapp.repository.TaskRepository;
import mate.academy.taskmanagementapp.service.AuthServiceHelper;
import mate.academy.taskmanagementapp.service.dropbox.DropboxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;
    private final DropboxService dropboxService;
    private final TaskRepository taskRepository;
    private final AuthServiceHelper authServiceHelper;

    @Override
    @Transactional
    public AttachmentDto addAttachment(CreateAttachmentRequestDto dto) {
        MultipartFile file = dto.getFile();
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        Task task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        authServiceHelper.assertCanAccessTask(task);

        String dropboxFileId;
        try {
            dropboxFileId = dropboxService.uploadFile(file);
        } catch (RuntimeException e) {
            throw new DropboxException("Failed to upload file to Dropbox");
        }

        try {
            Attachment attachment = new Attachment();
            attachment.setTask(task);
            attachment.setDropboxFileId(dropboxFileId);
            attachment.setFileName(file.getOriginalFilename());
            attachment.setUploadDate(LocalDateTime.now());

            Attachment savedAttachment = attachmentRepository.save(attachment);
            return attachmentMapper.toDto(savedAttachment);
        } catch (RuntimeException e) {
            try {
                dropboxService.deleteFile(dropboxFileId);
            } catch (Exception ex) {
                log.warn("Failed to rollback Dropbox file after DB error. dropboxFileId={}",
                        dropboxFileId, ex);
            }
            throw e;
        }
    }

    @Override
    public List<AttachmentDto> findAllAttachmentsByTaskId(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        authServiceHelper.assertCanAccessTask(task);

        return attachmentRepository.findAllByTaskId(taskId)
                .stream()
                .map(attachmentMapper::toDto)
                .toList();
    }

    @Override
    public AttachmentDto getAttachmentById(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Attachment not found"));

        authServiceHelper.assertCanAccessTask(attachment.getTask());

        return attachmentMapper.toDto(attachment);
    }

    @Override
    public byte[] downloadAttachment(Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new EntityNotFoundException("Attachment not found"));

        authServiceHelper.assertCanAccessTask(attachment.getTask());

        return dropboxService.downloadFile(attachment.getDropboxFileId());
    }

    @Override
    @Transactional
    public void deleteAttachment(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Attachment not found"));

        authServiceHelper.assertCanAccessTask(attachment.getTask());

        String dropboxFileId = attachment.getDropboxFileId();
        if (dropboxFileId != null && !dropboxFileId.isBlank()) {
            try {
                dropboxService.deleteFile(dropboxFileId);
            } catch (Exception e) {
                log.warn("Dropbox file delete failed for id={}", dropboxFileId, e);
            }
        }

        attachmentRepository.delete(attachment);
    }
}
