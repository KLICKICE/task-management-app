package mate.academy.taskmanagementapp.service.label;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import mate.academy.taskmanagementapp.dto.label.CreateLabelRequestDto;
import mate.academy.taskmanagementapp.dto.label.LabelDto;
import mate.academy.taskmanagementapp.dto.label.UpdateLabelDto;
import mate.academy.taskmanagementapp.mapper.LabelMapper;
import mate.academy.taskmanagementapp.model.label.Label;
import mate.academy.taskmanagementapp.repository.LabelRepository;
import lombok.extern.slf4j.Slf4j;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

        when(labelMapper.toEntity(createLabelRequestDto)).thenReturn(label);
        when(labelRepository.save(label)).thenReturn(label);
        when(labelMapper.toDto(label)).thenReturn(labelDto);

        LabelDto actualLabel = labelService.createLabel(createLabelRequestDto);

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

        when(labelRepository.findById(id)).thenReturn(Optional.of(label));
        when(labelRepository.save(label)).thenReturn(label);
        when(labelMapper.toDto(label)).thenReturn(labelDto);

        LabelDto actual = labelService.updateLabelFromDto(id, updateLabelDto);

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

        when(labelRepository.findById(id)).thenReturn(Optional.of(label));

        assertDoesNotThrow(() -> labelService.deleteLabel(id));

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

        when(labelRepository.findAll()).thenReturn(List.of(label));
        when(labelMapper.toDto(label)).thenReturn(labelDto);

        List<LabelDto> actual = labelService.getAllLabels();

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals("Sunless", actual.get(0).getName());
        assertEquals("Black", actual.get(0).getColor());

        verify(labelRepository).findAll();
        verify(labelMapper).toDto(label);
    }

}