package mate.academy.taskmanagementapp.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementapp.dto.comment.CommentDto;
import mate.academy.taskmanagementapp.dto.comment.CreateCommentRequestDto;
import mate.academy.taskmanagementapp.service.comment.CommentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Comment management", description = "Operations related to comments")
public class CommentController {
    private final CommentService commentService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public CommentDto addComment(@RequestBody CreateCommentRequestDto requestDto) {
        return commentService.addComment(requestDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public List<CommentDto> getCommentsByTaskId(@RequestParam Long taskId) {
        return commentService.getCommentsByTaskId(taskId);
    }
}

