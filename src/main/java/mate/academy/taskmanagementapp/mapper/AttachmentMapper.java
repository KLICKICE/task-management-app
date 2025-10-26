package mate.academy.taskmanagementapp.mapper;

import mate.academy.taskmanagementapp.config.MapConfig;
import mate.academy.taskmanagementapp.dto.attachment.AttachmentDto;
import mate.academy.taskmanagementapp.dto.attachment.CreateAttachmentRequestDto;
import mate.academy.taskmanagementapp.model.attachment.Attachment;
import org.mapstruct.Mapper;

@Mapper(config = MapConfig.class)
public interface AttachmentMapper {
    Attachment toEntity(CreateAttachmentRequestDto createAttachmentRequestDto);

    AttachmentDto toDto(Attachment attachment);
}
