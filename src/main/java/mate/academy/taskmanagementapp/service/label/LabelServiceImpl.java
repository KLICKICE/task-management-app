package mate.academy.taskmanagementapp.service.label;

import java.util.List;

import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementapp.dto.label.CreateLabelRequestDto;
import mate.academy.taskmanagementapp.dto.label.LabelDto;
import mate.academy.taskmanagementapp.dto.label.UpdateLabelDto;
import mate.academy.taskmanagementapp.exception.EntityNotFoundException;
import mate.academy.taskmanagementapp.mapper.LabelMapper;
import mate.academy.taskmanagementapp.model.label.Label;
import mate.academy.taskmanagementapp.repository.LabelRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    @Override
    public LabelDto createLabel(CreateLabelRequestDto createLabelRequestDto) {
        Label entity = labelMapper.toEntity(createLabelRequestDto);
        return labelMapper.toDto(labelRepository.save(entity));
    }

    @Override
    public List<LabelDto> getAllLabels() {
        return labelRepository.findAll()
                .stream().map(labelMapper::toDto).toList();
    }

    @Override
    public void updateLabelFromDto(Long id, UpdateLabelDto updateLabelDto) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Label not found"));
        labelMapper.updateLabelFromDto(updateLabelDto, label);
        labelRepository.save(label);
    }

    @Override
    public void deleteLabel(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Label not found"));
        labelRepository.delete(label);
    }
}
