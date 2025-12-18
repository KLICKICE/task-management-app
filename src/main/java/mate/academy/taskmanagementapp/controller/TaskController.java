package mate.academy.taskmanagementapp.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementapp.dto.label.LabelDto;
import mate.academy.taskmanagementapp.dto.task.CreateTaskRequestDto;
import mate.academy.taskmanagementapp.dto.task.TaskDto;
import mate.academy.taskmanagementapp.dto.task.TaskUpdatedDto;
import mate.academy.taskmanagementapp.service.task.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Task management", description = "Operations related to tasks")
public class TaskController {
    private final TaskService taskService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto createTask(@RequestBody @Valid CreateTaskRequestDto createTaskRequestDto) {
        return taskService.createTask(createTaskRequestDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto updateTask(@PathVariable Long id,
                              @RequestBody @Valid TaskUpdatedDto taskUpdatedDto) {
        return taskService.updateTask(id, taskUpdatedDto);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDto> getTasks(@RequestParam Long projectId) {
        return taskService.getTasksByProjectId(projectId);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/{taskId}/labels/{labelId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto addLabelToTask(@PathVariable Long taskId, @PathVariable Long labelId) {
        return taskService.addLabelToTask(taskId, labelId);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{taskId}/labels/{labelId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto removeLabelFromTask(@PathVariable Long taskId, @PathVariable Long labelId) {
        return taskService.removeLabelFromTask(taskId, labelId);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}/labels")
    @ResponseStatus(HttpStatus.OK)
    public List<LabelDto> getTaskLabels(@PathVariable Long id) {
        return taskService.getTaskLabels(id);
    }
}
