package mate.academy.taskmanagementapp.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementapp.dto.attachment.AttachmentDto;
import mate.academy.taskmanagementapp.dto.attachment.CreateAttachmentRequestDto;
import mate.academy.taskmanagementapp.service.attachment.AttachmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
@Tag(name = "Attachment management", description = "Operations related to attachments")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AttachmentDto addAttachment(
            @RequestParam Long taskId,
            @RequestPart("file") MultipartFile file
    ) {
        CreateAttachmentRequestDto dto = new CreateAttachmentRequestDto();
        dto.setTaskId(taskId);
        dto.setFile(file);
        return attachmentService.addAttachment(dto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AttachmentDto> getAttachmentsByTaskId(@RequestParam Long taskId) {
        return attachmentService.findAllAttachmentsByTaskId(taskId);
    }
}
