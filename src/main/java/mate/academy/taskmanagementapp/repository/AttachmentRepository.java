package mate.academy.taskmanagementapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mate.academy.taskmanagementapp.model.attachment.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
