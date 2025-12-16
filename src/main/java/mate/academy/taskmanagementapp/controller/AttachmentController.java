package mate.academy.taskmanagementapp.controller;

import java.nio.charset.StandardCharsets;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementapp.dto.attachment.AttachmentDto;
import mate.academy.taskmanagementapp.dto.attachment.CreateAttachmentRequestDto;
import mate.academy.taskmanagementapp.service.attachment.AttachmentService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable Long id) {
        AttachmentDto meta = attachmentService.getAttachmentById(id);
        byte[] fileBytes = attachmentService.downloadAttachment(id);

        String filename = (meta.getFileName() == null || meta.getFileName().isBlank())
                ? "attachment-" + id
                : meta.getFileName();

        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(filename, StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(fileBytes.length)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .body(fileBytes);
    }
}
