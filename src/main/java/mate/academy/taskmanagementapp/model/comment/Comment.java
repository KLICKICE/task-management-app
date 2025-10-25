package mate.academy.taskmanagementapp.model.comment;

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
import mate.academy.taskmanagementapp.model.user.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(nullable = false, length = 1000)
    private String text;

    @Column(nullable = false)
    private LocalDateTime timeStamp;

    @PrePersist
    public void setTimestamp() {
        if (timeStamp == null) {
            timeStamp = LocalDateTime.now();
        }
    }
}
