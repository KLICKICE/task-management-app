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
import mate.academy.taskmanagementapp.repository.AttachmentRepository;
import mate.academy.taskmanagementapp.repository.TaskRepository;
import mate.academy.taskmanagementapp.service.dropbox.DropboxService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;
    private final DropboxService dropboxService;
    private final TaskRepository taskRepository;

    @Override
    public AttachmentDto addAttachment(CreateAttachmentRequestDto dto) {
        MultipartFile file = dto.getFile();
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        String dropboxFileId;
        try {
            dropboxFileId = dropboxService.uploadFile(file);
        } catch (RuntimeException e) {
            throw new DropboxException("Failed to upload file to Dropbox");
        }

        Attachment attachment = new Attachment();
        attachment.setTask(taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new EntityNotFoundException("Task not found")));
        attachment.setDropboxFileId(dropboxFileId);
        attachment.setFileName(file.getOriginalFilename());
        attachment.setUploadDate(LocalDateTime.now());

        Attachment savedAttachment = attachmentRepository.save(attachment);
        return attachmentMapper.toDto(savedAttachment);
    }

    @Override
    public List<AttachmentDto> findAllAttachmentsByTaskId(Long taskId) {
        return attachmentRepository.findAllByTaskId(taskId)
                .stream()
                .map(attachmentMapper::toDto)
                .toList();
    }
}
