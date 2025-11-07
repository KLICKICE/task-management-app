package mate.academy.taskmanagementapp.service.attachment;

import java.util.List;
import java.util.Optional;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
        when(dropboxService.uploadFile(multipartFile)).thenReturn("dropbox123");
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(attachmentRepository.save(any(Attachment.class))).thenReturn(attachment);
        when(attachmentMapper.toDto(attachment)).thenReturn(attachmentDto);

        AttachmentDto actual = attachmentService.addAttachment(createAttachmentRequestDto);

        assertNotNull(actual);
        assertEquals("test.txt", actual.getFileName());
        assertEquals(1L, actual.getId());

        verify(dropboxService).uploadFile(multipartFile);
        verify(taskRepository).findById(id);
        verify(attachmentRepository).save(any(Attachment.class));
        verify(attachmentMapper).toDto(attachment);
    }

    @Test
    @DisplayName("Find all attachments by Task Id, success")
    void findAllAttachments_ByTaskId_success() {
        Long taskId = 1L;

        when(attachmentRepository.findAllByTaskId(taskId)).thenReturn(List.of(attachment));
        when(attachmentMapper.toDto(attachment)).thenReturn(attachmentDto);

        List<AttachmentDto> result = attachmentService.findAllAttachmentsByTaskId(taskId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test.txt", result.get(0).getFileName());

        verify(attachmentRepository).findAllByTaskId(taskId);
        verify(attachmentMapper).toDto(attachment);
        verifyNoMoreInteractions(attachmentRepository, attachmentMapper);
    }
}
