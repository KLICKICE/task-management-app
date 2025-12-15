package mate.academy.taskmanagementapp.service.attachment;

import java.util.List;
import java.util.Optional;

import mate.academy.taskmanagementapp.service.AuthServiceHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import mate.academy.taskmanagementapp.dto.attachment.AttachmentDto;
import mate.academy.taskmanagementapp.dto.attachment.CreateAttachmentRequestDto;
import mate.academy.taskmanagementapp.mapper.AttachmentMapper;
import mate.academy.taskmanagementapp.model.attachment.Attachment;
import mate.academy.taskmanagementapp.model.task.Task;
import mate.academy.taskmanagementapp.repository.AttachmentRepository;
import mate.academy.taskmanagementapp.repository.TaskRepository;
import mate.academy.taskmanagementapp.service.dropbox.DropboxService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttachmentServiceImplTest {

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private AttachmentMapper attachmentMapper;

    @Mock
    private DropboxService dropboxService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private AuthServiceHelper authServiceHelper;

    @InjectMocks
    private AttachmentServiceImpl attachmentService;

    private Task task;
    private Attachment attachment;
    private AttachmentDto attachmentDto;
    private CreateAttachmentRequestDto createAttachmentRequestDto;
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Implement file upload");

        attachment = new Attachment();
        attachment.setId(1L);
        attachment.setDropboxFileId("dropbox123");
        attachment.setFileName("test.txt");
        attachment.setTask(task);

        attachmentDto = new AttachmentDto();
        attachmentDto.setId(1L);
        attachmentDto.setFileName("test.txt");

        multipartFile = mock(MultipartFile.class);

        createAttachmentRequestDto = new CreateAttachmentRequestDto();
        createAttachmentRequestDto.setTaskId(1L);
        createAttachmentRequestDto.setFile(multipartFile);
    }

    @Test
    @DisplayName("Add attachment, success")
    void addAttachment_success() {
        Long id = 1L;

        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");
        when(multipartFile.isEmpty()).thenReturn(false);

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        doNothing().when(authServiceHelper).assertCanAccessTask(task);

        when(dropboxService.uploadFile(multipartFile)).thenReturn("dropbox123");
        when(attachmentRepository.save(any(Attachment.class))).thenReturn(attachment);
        when(attachmentMapper.toDto(attachment)).thenReturn(attachmentDto);

        AttachmentDto actual = attachmentService.addAttachment(createAttachmentRequestDto);

        assertNotNull(actual);
        assertEquals("test.txt", actual.getFileName());
        assertEquals(1L, actual.getId());

        verify(taskRepository).findById(id);
        verify(authServiceHelper).assertCanAccessTask(task);
        verify(dropboxService).uploadFile(multipartFile);
        verify(attachmentRepository).save(any(Attachment.class));
        verify(attachmentMapper).toDto(attachment);
        verifyNoMoreInteractions(taskRepository, dropboxService, attachmentRepository, attachmentMapper, authServiceHelper);
    }

    @Test
    @DisplayName("Find all attachments by Task Id, success")
    void findAllAttachments_ByTaskId_success() {
        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        doNothing().when(authServiceHelper).assertCanAccessTask(task);

        when(attachmentRepository.findAllByTaskId(taskId)).thenReturn(List.of(attachment));
        when(attachmentMapper.toDto(attachment)).thenReturn(attachmentDto);

        List<AttachmentDto> result = attachmentService.findAllAttachmentsByTaskId(taskId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test.txt", result.get(0).getFileName());

        verify(taskRepository).findById(taskId);
        verify(authServiceHelper).assertCanAccessTask(task);
        verify(attachmentRepository).findAllByTaskId(taskId);
        verify(attachmentMapper).toDto(attachment);
        verifyNoMoreInteractions(taskRepository, attachmentRepository, attachmentMapper, authServiceHelper);
        verifyNoInteractions(dropboxService);
    }

    @Test
    @DisplayName("Get attachment by id, success")
    void getAttachmentById_success() {
        Long attachmentId = 1L;

        when(attachmentRepository.findById(attachmentId)).thenReturn(Optional.of(attachment));
        doNothing().when(authServiceHelper).assertCanAccessTask(task);
        when(attachmentMapper.toDto(attachment)).thenReturn(attachmentDto);

        AttachmentDto result = attachmentService.getAttachmentById(attachmentId);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test.txt", result.getFileName());

        verify(attachmentRepository).findById(attachmentId);
        verify(authServiceHelper).assertCanAccessTask(task);
        verify(attachmentMapper).toDto(attachment);
        verifyNoMoreInteractions(attachmentRepository, attachmentMapper, authServiceHelper);
        verifyNoInteractions(taskRepository, dropboxService);
    }

    @Test
    @DisplayName("Download attachment, success")
    void downloadAttachment_success() {
        Long attachmentId = 1L;

        when(attachmentRepository.findById(attachmentId)).thenReturn(Optional.of(attachment));
        doNothing().when(authServiceHelper).assertCanAccessTask(task);
        when(dropboxService.downloadFile("dropbox123")).thenReturn(new byte[] {1, 2, 3});

        byte[] result = attachmentService.downloadAttachment(attachmentId);

        assertNotNull(result);
        assertEquals(3, result.length);

        verify(attachmentRepository).findById(attachmentId);
        verify(authServiceHelper).assertCanAccessTask(task);
        verify(dropboxService).downloadFile("dropbox123");
        verifyNoMoreInteractions(attachmentRepository, authServiceHelper, dropboxService);
        verifyNoInteractions(taskRepository, attachmentMapper);
    }

    @Test
    @DisplayName("Delete attachment, success")
    void deleteAttachment_success() {
        Long attachmentId = 1L;

        when(attachmentRepository.findById(attachmentId)).thenReturn(Optional.of(attachment));
        doNothing().when(authServiceHelper).assertCanAccessTask(task);

        attachmentService.deleteAttachment(attachmentId);

        verify(attachmentRepository).findById(attachmentId);
        verify(authServiceHelper).assertCanAccessTask(task);
        verify(dropboxService).deleteFile("dropbox123");
        verify(attachmentRepository).delete(attachment);
        verifyNoMoreInteractions(attachmentRepository, authServiceHelper, dropboxService);
        verifyNoInteractions(taskRepository, attachmentMapper);
    }
}
