package mate.academy.taskmanagementapp.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.jdbc.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import mate.academy.taskmanagementapp.model.label.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LabelRepositoryTest {

    @Autowired
    private LabelRepository labelRepository;

    @Test
    @DisplayName("""
            Save a label, success 
            """)
    void saveLabel_success() {
        Label label = new Label();
        label.setColor("Black");
        label.setName("Deprecated");

        Label savedLabel = labelRepository.save(label);

        assertNotNull(savedLabel.getId());
        assertEquals("Black", savedLabel.getColor());
        assertEquals("Deprecated", savedLabel.getName());
    }

    @Test
    @DisplayName("""
            Find label by id success
            """)
    void findLabelById_success() {
        Label label = new Label();
        label.setColor("Black");
        label.setName("Deprecated");

        Label savedLabel = labelRepository.save(label);

        assertTrue(labelRepository.findById(savedLabel.getId()).isPresent());
    }
}