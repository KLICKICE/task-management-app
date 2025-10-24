package mate.academy.taskmanagementapp.controller;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementapp.dto.task.CreateTaskRequestDto;
import mate.academy.taskmanagementapp.dto.task.TaskDto;
import mate.academy.taskmanagementapp.dto.task.TaskUpdatedDto;
import mate.academy.taskmanagementapp.model.TaskPriority;
import mate.academy.taskmanagementapp.model.TaskStatus;
import mate.academy.taskmanagementapp.service.task.TaskService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Task management", description = "Operations related to tasks")
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/createTask")
    TaskDto createTask(@RequestBody CreateTaskRequestDto createTaskRequestDto) {
        return taskService.createTask(createTaskRequestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/user/{id}")
    List<TaskDto> findAllByAssignedUser(@PathVariable Long id) {
        return taskService.findAllByAssignedUser(id);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/status")
    List<TaskDto> findAllByStatus(@RequestParam TaskStatus.StatusTask status) {
        return taskService.findAllByStatus(status);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/findByPriority")
    List<TaskDto> findAllByPriority(@RequestParam TaskPriority.PriorityStatus priority) {
        return taskService.findAllByPriority(priority);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/deadLine")
    List<TaskDto> findAllByDeadlineBefore(@RequestParam
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                          LocalDateTime date) {
        return taskService.findAllByDeadlineBefore(date);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{id}")
    TaskDto updateTask(@PathVariable Long id, @RequestBody TaskUpdatedDto taskUpdatedDto) {
        return taskService.updateTask(id, taskUpdatedDto);
    }
}
