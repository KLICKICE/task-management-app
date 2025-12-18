package mate.academy.taskmanagementapp.mapper;

import mate.academy.taskmanagementapp.config.MapConfig;
import mate.academy.taskmanagementapp.dto.project.CreateProjectRequestDto;
import mate.academy.taskmanagementapp.dto.project.ProjectDto;
import mate.academy.taskmanagementapp.dto.project.ProjectUpdateDto;
import mate.academy.taskmanagementapp.model.project.Project;
import mate.academy.taskmanagementapp.model.project.ProjectStatus;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapConfig.class)
public interface ProjectMapper {

    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "endDate", source = "endDate")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Project toEntity(CreateProjectRequestDto createProjectRequestDto);

    @Mapping(target = "ownerId",
            expression = "java(project.getOwner() != null ? project.getOwner().getId() : null)")
    @Mapping(target = "projectStatus", source = "status", qualifiedByName = "statusToString")
    ProjectDto toDto(Project project);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateProjectFromDto(ProjectUpdateDto projectUpdateDto, @MappingTarget Project project);

    @Named("statusToString")
    default String statusToString(ProjectStatus status) {
        return status == null ? null : status.name();
    }
}
