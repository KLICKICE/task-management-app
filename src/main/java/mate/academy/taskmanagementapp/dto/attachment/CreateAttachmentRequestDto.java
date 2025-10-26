package mate.academy.taskmanagementapp.dto.attachment;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateAttachmentRequestDto {
    private Long taskId;
    private MultipartFile file;
}
