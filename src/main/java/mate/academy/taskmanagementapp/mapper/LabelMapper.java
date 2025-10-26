package mate.academy.taskmanagementapp.mapper;

import mate.academy.taskmanagementapp.config.MapConfig;
import mate.academy.taskmanagementapp.dto.label.CreateLabelRequestDto;
import mate.academy.taskmanagementapp.dto.label.LabelDto;
import mate.academy.taskmanagementapp.dto.label.UpdateLabelDto;
import mate.academy.taskmanagementapp.model.label.Label;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapConfig.class)
public interface LabelMapper {
    Label toEntity(CreateLabelRequestDto createLabelRequestDto);

    LabelDto toDto(Label label);

    void updateLabelFromDto(UpdateLabelDto updateLabelDto, @MappingTarget Label label);
}
