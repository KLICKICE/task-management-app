package mate.academy.taskmanagementapp.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import mate.academy.taskmanagementapp.dto.label.CreateLabelRequestDto;
import mate.academy.taskmanagementapp.dto.label.LabelDto;
import mate.academy.taskmanagementapp.dto.label.UpdateLabelDto;
import mate.academy.taskmanagementapp.service.label.LabelService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Label management", description = "Operations related to labels")
@RestController
@RequestMapping("/api/labels")
@RequiredArgsConstructor
public class LabelController {
    private final LabelService labelService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    LabelDto createLabel(@RequestBody CreateLabelRequestDto createLabelRequestDto) {
        return labelService.createLabel(createLabelRequestDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    List<LabelDto> getAllLabels() {
        return labelService.getAllLabels();
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{id}")
    void updateLabelFromDto(
            @PathVariable Long id,
            @RequestBody UpdateLabelDto updateLabelDto
    ) {
        labelService.updateLabelFromDto(id, updateLabelDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{id}")
    void deleteLabel(@PathVariable Long id) {
        labelService.deleteLabel(id);
    }
}
