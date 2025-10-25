package mate.academy.taskmanagementapp.model.project;

import java.time.LocalDate;
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
import lombok.Getter;
import lombok.Setter;
import mate.academy.taskmanagementapp.model.user.User;

@Entity
@Getter
@Setter
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDate startDate;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    private String description;

    @Column
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private ProjectStatus status;

    @PrePersist
    public void setCreationDate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
