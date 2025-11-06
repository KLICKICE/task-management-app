package mate.academy.taskmanagementapp.service.label;

import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import mate.academy.taskmanagementapp.dto.label.*;
import mate.academy.taskmanagementapp.mapper.*;
import mate.academy.taskmanagementapp.model.label.*;
import mate.academy.taskmanagementapp.repository.*;
import lombok.extern.slf4j.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class LabelServiceImplTest {

    @Mock
    private LabelRepository labelRepository;

    @Mock
    private LabelMapper labelMapper;

    @InjectMocks
    private LabelServiceImpl labelService;

    private Label label;

    @BeforeEach
    void setUp() {
        label = new Label();
        label.setName("Sunless");
        label.setColor("Black");
    }

    @Test
    @DisplayName("Create label successfully")
    void createLabel_success() {
        Long id = 1L;

        CreateLabelRequestDto createLabelRequestDto = new CreateLabelRequestDto();
        createLabelRequestDto.setName("Sunless");
        createLabelRequestDto.setColor("Black");

        LabelDto labelDto = new LabelDto();
        labelDto.setId(id);
        labelDto.setName("Sunless");
        labelDto.setColor("Black");

        // given
        when(labelMapper.toEntity(createLabelRequestDto)).thenReturn(label);
        when(labelRepository.save(label)).thenReturn(label);
        when(labelMapper.toDto(label)).thenReturn(labelDto);

        // when
        LabelDto actualLabel = labelService.createLabel(createLabelRequestDto);

        // then
        assertNotNull(actualLabel);
        assertEquals("Sunless", actualLabel.getName());
        assertEquals("Black", actualLabel.getColor());

        verify(labelMapper).toEntity(createLabelRequestDto);
        verify(labelRepository).save(label);
        verify(labelMapper).toDto(label);
    }

    @Test
    @DisplayName("Update label, success")
    void updateLabel_Success() {
        Long id = 1L;
        label.setId(id);

        UpdateLabelDto updateLabelDto = new UpdateLabelDto();
        updateLabelDto.setName("Updated Sunless");
        updateLabelDto.setColor("White");

        LabelDto labelDto = new LabelDto();
        labelDto.setId(id);
        labelDto.setName("Updated Sunless");
        labelDto.setColor("White");

        // given
        when(labelRepository.findById(id)).thenReturn(Optional.of(label));
        when(labelRepository.save(label)).thenReturn(label);
        when(labelMapper.toDto(label)).thenReturn(labelDto);

        // when
        LabelDto actual = labelService.updateLabelFromDto(id, updateLabelDto);

        // then
        assertNotNull(actual);
        assertEquals("Updated Sunless", actual.getName());
        assertEquals("White", actual.getColor());
        verify(labelRepository).findById(id);
        verify(labelMapper).updateLabelFromDto(updateLabelDto, label);
        verify(labelRepository).save(label);
        verify(labelMapper).toDto(label);
    }

    @Test
    @DisplayName("Delete label by Id, success")
    void deleteLabelById_Success() {
        Long id = 1L;
        label.setId(id);

        // given
        when(labelRepository.findById(id)).thenReturn(Optional.of(label));

        // when & then
        assertDoesNotThrow(() -> labelService.deleteLabel(id));

        // verify interactions
        verify(labelRepository).findById(id);
        verify(labelRepository).delete(label);
    }

    @Test
    @DisplayName("Get all labels, success")
    void getAllLabels_Success() {
        Long id = 1L;
        label.setId(id);

        LabelDto labelDto = new LabelDto();
        labelDto.setId(id);
        labelDto.setName("Sunless");
        labelDto.setColor("Black");

        // given
        when(labelRepository.findAll()).thenReturn(List.of(label));
        when(labelMapper.toDto(label)).thenReturn(labelDto);

        // when
        List<LabelDto> actual = labelService.getAllLabels();

        // then
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals("Sunless", actual.get(0).getName());
        assertEquals("Black", actual.get(0).getColor());

        verify(labelRepository).findAll();
        verify(labelMapper).toDto(label);
    }

}