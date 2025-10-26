package mate.academy.taskmanagementapp.dto.attachment;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AttachmentDto {
    private Long id;
    private Long taskId;
    private String dropboxFileId;
    private String fileName;
    private LocalDateTime uploadDate;
}
