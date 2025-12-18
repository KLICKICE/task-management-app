package mate.academy.taskmanagementapp.service.task;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mate.academy.taskmanagementapp.dto.label.LabelDto;
import mate.academy.taskmanagementapp.dto.task.CreateTaskRequestDto;
import mate.academy.taskmanagementapp.dto.task.TaskDto;
import mate.academy.taskmanagementapp.dto.task.TaskUpdatedDto;
import mate.academy.taskmanagementapp.exception.AccessDeniedException;
import mate.academy.taskmanagementapp.exception.EntityNotFoundException;
import mate.academy.taskmanagementapp.mapper.LabelMapper;
import mate.academy.taskmanagementapp.mapper.TaskMapper;
import mate.academy.taskmanagementapp.model.attachment.Attachment;
import mate.academy.taskmanagementapp.model.label.Label;
import mate.academy.taskmanagementapp.model.project.Project;
import mate.academy.taskmanagementapp.model.task.Task;
import mate.academy.taskmanagementapp.model.task.TaskPriority;
import mate.academy.taskmanagementapp.model.task.TaskStatus;
import mate.academy.taskmanagementapp.model.user.User;
import mate.academy.taskmanagementapp.repository.AttachmentRepository;
import mate.academy.taskmanagementapp.repository.LabelRepository;
import mate.academy.taskmanagementapp.repository.ProjectRepository;
import mate.academy.taskmanagementapp.repository.TaskRepository;
import mate.academy.taskmanagementapp.repository.UserRepository;
import mate.academy.taskmanagementapp.service.AuthServiceHelper;
import mate.academy.taskmanagementapp.service.dropbox.DropboxService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final AuthServiceHelper authServiceHelper;
    private final ProjectRepository projectRepository;
    private final DropboxService dropboxService;
    private final AttachmentRepository attachmentRepository;
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    @Override
    @Transactional
    public TaskDto createTask(CreateTaskRequestDto dto) {
        User currentUser = authServiceHelper.getCurrentUser();

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Project not found: projectId=" + dto.getProjectId()
                ));

        boolean isAdmin = authServiceHelper.isAdmin(currentUser);
        boolean isProjectOwner = project.getOwner() != null
                && project.getOwner().getId() != null
                && project.getOwner().getId().equals(currentUser.getId());

        if (!isAdmin && !isProjectOwner) {
            throw new AccessDeniedException("You don't have access to projectId="
                    + dto.getProjectId());
        }

        User assignee = userRepository.findById(dto.getAssignedUserId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found: userId=" + dto.getAssignedUserId()
                ));

        Task task = taskMapper.toEntity(dto);
        task.setProject(project);
        task.setAssignedUser(assignee);

        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.NEW);
        }
        if (task.getPriority() == null) {
            task.setPriority(TaskPriority.MEDIUM);
        }
        if (task.getDeadline() == null) {
            task.setDeadline(LocalDateTime.now().plusDays(1));
        }

        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: id=" + id));
        return taskMapper.toDto(task);
    }

    @Override
    @Transactional
    public TaskDto updateTask(Long id, TaskUpdatedDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: id=" + id));

        User currentUser = authServiceHelper.getCurrentUser();
        validateUserPermission(task, currentUser);

        taskMapper.updateTaskFromDto(dto, task);

        if (dto.getAssignedUserId() != null) {
            User user = userRepository.findById(dto.getAssignedUserId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "User not found: userId=" + dto.getAssignedUserId()
                    ));
            task.setAssignedUser(user);
        } else if (dto.getAssignedUserEmail() != null) {
            User user = userRepository.findByEmail(dto.getAssignedUserEmail())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "User not found: email=" + dto.getAssignedUserEmail()
                    ));
            task.setAssignedUser(user);
        }

        if (dto.getTaskStatus() != null && !dto.getTaskStatus().isBlank()) {
            task.setStatus(parseStatus(dto.getTaskStatus()));
        }

        if (dto.getTaskPriority() != null && !dto.getTaskPriority().isBlank()) {
            task.setPriority(parsePriority(dto.getTaskPriority()));
        }

        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: id=" + id));

        User currentUser = authServiceHelper.getCurrentUser();
        validateUserPermission(task, currentUser);

        List<Attachment> attachments = attachmentRepository.findAllByTaskId(task.getId());

        for (Attachment attachment : attachments) {
            String dropboxFileId = attachment.getDropboxFileId();
            if (dropboxFileId == null || dropboxFileId.isBlank()) {
                continue;
            }
            try {
                dropboxService.deleteFile(dropboxFileId);
            } catch (Exception e) {
                log.warn("Failed to delete Dropbox file: {}", dropboxFileId, e);
            }
        }

        attachmentRepository.deleteAll(attachments);
        taskRepository.delete(task);

        log.info("Task {} deleted by user {}", id, currentUser.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDto> getTasksByProjectId(Long projectId) {
        User currentUser = authServiceHelper.getCurrentUser();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: projectId="
                        + projectId));

        boolean isAdmin = authServiceHelper.isAdmin(currentUser);
        boolean isOwner = project.getOwner() != null
                && project.getOwner().getId() != null
                && project.getOwner().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("You don't have access to projectId=" + projectId);
        }

        return taskRepository.findAllByProjectId(projectId).stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public TaskDto addLabelToTask(Long taskId, Long labelId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: taskId="
                        + taskId));
        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new EntityNotFoundException("Label not found: labelId="
                        + labelId));

        authServiceHelper.assertCanAccessTask(task);

        task.getLabels().add(label);
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskDto removeLabelFromTask(Long taskId, Long labelId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: taskId=" + taskId));
        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new EntityNotFoundException("Label not found: labelId="
                        + labelId));

        authServiceHelper.assertCanAccessTask(task);

        task.getLabels().remove(label);
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelDto> getTaskLabels(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: taskId=" + taskId));

        authServiceHelper.assertCanAccessTask(task);

        return task.getLabels().stream()
                .map(labelMapper::toDto)
                .toList();
    }

    private void validateUserPermission(Task task, User currentUser) {
        boolean isAdmin = authServiceHelper.isAdmin(currentUser);

        boolean isAssignee = task.getAssignedUser() != null
                && task.getAssignedUser().getId() != null
                && task.getAssignedUser().getId().equals(currentUser.getId());

        boolean isProjectOwner = task.getProject() != null
                && task.getProject().getOwner() != null
                && task.getProject().getOwner().getId() != null
                && task.getProject().getOwner().getId().equals(currentUser.getId());

        if (!isAdmin && !isAssignee && !isProjectOwner) {
            throw new AccessDeniedException("You are not allowed to modify taskId=" + task.getId());
        }
    }

    private TaskStatus parseStatus(String raw) {
        try {
            return TaskStatus.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Task status not found: status=" + raw);
        }
    }

    private TaskPriority parsePriority(String raw) {
        try {
            return TaskPriority.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Task priority not found: priority=" + raw);
        }
    }
}
