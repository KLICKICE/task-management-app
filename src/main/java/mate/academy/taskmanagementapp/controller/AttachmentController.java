package mate.academy.taskmanagementapp.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementapp.dto.attachment.AttachmentDto;
import mate.academy.taskmanagementapp.dto.attachment.CreateAttachmentRequestDto;
import mate.academy.taskmanagementapp.service.attachment.AttachmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
@Tag(name = "Attachment management", description = "Operations related to attachments")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public AttachmentDto addAttachment(@RequestBody CreateAttachmentRequestDto dto) {
        return attachmentService.addAttachment(dto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public List<AttachmentDto> getAttachmentsByTaskId(@RequestParam Long taskId) {
        return attachmentService.findAllAttachmentsByTaskId(taskId);
    }
}

