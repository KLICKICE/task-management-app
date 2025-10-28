package mate.academy.taskmanagementapp.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementapp.dto.label.CreateLabelRequestDto;
import mate.academy.taskmanagementapp.dto.label.LabelDto;
import mate.academy.taskmanagementapp.dto.label.UpdateLabelDto;
import mate.academy.taskmanagementapp.service.label.LabelService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/labels")
@RequiredArgsConstructor
@Tag(name = "Label management", description = "Operations related to labels")
public class LabelController {
    private final LabelService labelService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public LabelDto createLabel(@RequestBody CreateLabelRequestDto requestDto) {
        return labelService.createLabel(requestDto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public List<LabelDto> getAllLabels() {
        return labelService.getAllLabels();
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{id}")
    public LabelDto updateLabel(@PathVariable Long id,
                                @RequestBody UpdateLabelDto dto) {
        return labelService.updateLabelFromDto(id, dto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteLabel(@PathVariable Long id) {
        labelService.deleteLabel(id);
    }
}

