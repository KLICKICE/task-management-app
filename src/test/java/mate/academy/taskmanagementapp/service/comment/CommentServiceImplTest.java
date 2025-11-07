package mate.academy.taskmanagementapp.service.comment;

import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import mate.academy.taskmanagementapp.dto.comment.*;
import mate.academy.taskmanagementapp.mapper.*;
import mate.academy.taskmanagementapp.model.comment.*;
import mate.academy.taskmanagementapp.model.role.*;
import mate.academy.taskmanagementapp.model.task.*;
import mate.academy.taskmanagementapp.model.user.*;
import mate.academy.taskmanagementapp.repository.*;
import mate.academy.taskmanagementapp.service.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private AuthServiceHelper authServiceHelper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private User user;
    private Role role;
    private Task task;
    private Comment comment;
    private CommentDto commentDto;
    private CreateCommentRequestDto createCommentRequestDto;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleName(Role.RoleName.USER);

        user = new User();
        user.setId(1L);
        user.setUsername("LostFromLight");
        user.setPassword("encodedPassword");
        user.setRoles(Set.of(role));

        task = new Task();
        task.setId(1L);
        task.setTitle("Implement authentication");
        task.setDescription("Develop secure login and registration flow.");
        task.setAssignedUser(user);

        comment = new Comment();
        comment.setId(1L);
        comment.setText("Authentication logic implemented successfully!");
        comment.setTask(task);
        comment.setUser(user);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setTaskId(1L);
        commentDto.setUserId(1L);
        commentDto.setText("Authentication logic implemented successfully!");

        createCommentRequestDto = new CreateCommentRequestDto();
        createCommentRequestDto.setTaskId(1L);
        createCommentRequestDto.setText("Authentication logic implemented successfully!");
    }

    @Test
    @DisplayName("Add comment, success")
    void addComment_success() {
        Long id = 1L;

        when(commentMapper.toEntity(createCommentRequestDto)).thenReturn(comment);
        when(taskRepository.findById(id)).thenReturn(Optional.ofNullable(task));
        when(authServiceHelper.getCurrentUser()).thenReturn(user);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        CommentDto actual = commentService.addComment(createCommentRequestDto);

        assertNotNull(actual);

        verify(commentMapper).toEntity(createCommentRequestDto);
        verify(taskRepository).findById(1L);
        verify(authServiceHelper).getCurrentUser();
        verify(commentRepository).save(comment);
        verify(commentMapper).toDto(comment);
    }

    @Test
    @DisplayName("Get comments by Task Id, success")
    void getComments_ByTaskId_success() {
        Long id = 1L;

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(commentRepository.findAllByTask(task)).thenReturn(List.of(comment));
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        List<CommentDto> actual = commentService.getCommentsByTaskId(id);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals("Authentication logic implemented successfully!",
                actual.get(0).getText());

        verify(taskRepository).findById(id);
        verify(commentRepository).findAllByTask(task);
        verify(commentMapper).toDto(comment);
    }
}
