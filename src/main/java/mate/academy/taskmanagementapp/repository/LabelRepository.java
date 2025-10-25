package mate.academy.taskmanagementapp.repository;

import mate.academy.taskmanagementapp.model.label.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Long> {
}
