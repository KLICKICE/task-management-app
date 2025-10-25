package mate.academy.taskmanagementapp.repository;

import mate.academy.taskmanagementapp.model.attachment.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
