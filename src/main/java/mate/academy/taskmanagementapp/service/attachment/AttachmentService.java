package mate.academy.taskmanagementapp.service.attachment;

import java.util.List;

import mate.academy.taskmanagementapp.dto.attachment.AttachmentDto;
import mate.academy.taskmanagementapp.dto.attachment.CreateAttachmentRequestDto;

public interface AttachmentService {
    AttachmentDto addAttachment(CreateAttachmentRequestDto createAttachmentRequestDto);

    List<AttachmentDto> findAllAttachmentsByTaskId(Long taskId);
}
