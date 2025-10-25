package mate.academy.taskmanagementapp.model.attachment;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import mate.academy.taskmanagementapp.model.task.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "attachments")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(nullable = false, unique = true)
    private String dropboxFileId;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private LocalDateTime uploadDate;

    @PrePersist
    public void setUploadDate() {
        if (uploadDate == null) {
            uploadDate = LocalDateTime.now();
        }
    }
}
