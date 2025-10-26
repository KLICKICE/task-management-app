package mate.academy.taskmanagementapp.service.label;

import java.util.List;

import mate.academy.taskmanagementapp.dto.label.CreateLabelRequestDto;
import mate.academy.taskmanagementapp.dto.label.LabelDto;
import mate.academy.taskmanagementapp.dto.label.UpdateLabelDto;

public interface LabelService {
    LabelDto createLabel(CreateLabelRequestDto createLabelRequestDto);

    List<LabelDto> getAllLabels();

    void updateLabelFromDto(Long id, UpdateLabelDto updateLabelDto);

    void deleteLabel(Long id);
}
