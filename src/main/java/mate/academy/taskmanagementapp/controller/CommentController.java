package mate.academy.taskmanagementapp.controller;

import java.util.List;
import org.springframework.security.access.prepost.*;
import org.springframework.web.bind.annotation.*;
import mate.academy.taskmanagementapp.dto.comment.CommentDto;
import mate.academy.taskmanagementapp.dto.comment.CreateCommentRequestDto;
import mate.academy.taskmanagementapp.service.comment.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Comment management", description = "Operations related to comments")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    CommentDto addComment(@RequestBody CreateCommentRequestDto requestDto) {
        return commentService.addComment(requestDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/task/{taskId}")
    List<CommentDto> getCommentsByTaskId(@PathVariable Long taskId) {
        return commentService.getCommentsByTaskId(taskId);
    }
}
