package mate.academy.taskmanagementapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mate.academy.taskmanagementapp.model.label.Label;

public interface LabelRepository extends JpaRepository<Label, Long> {
}
