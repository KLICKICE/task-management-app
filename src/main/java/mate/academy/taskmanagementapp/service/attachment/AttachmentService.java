package mate.academy.taskmanagementapp.service.attachment;

import java.util.List;

import mate.academy.taskmanagementapp.dto.attachment.AttachmentDto;
import mate.academy.taskmanagementapp.dto.attachment.CreateAttachmentRequestDto;

public interface AttachmentService {
    AttachmentDto addAttachment(CreateAttachmentRequestDto createAttachmentRequestDto);

    AttachmentDto getAttachmentById(Long id);

    List<AttachmentDto> findAllAttachmentsByTaskId(Long taskId);

    byte[] downloadAttachment(Long attachmentId);

    void deleteAttachment(Long id);
}
