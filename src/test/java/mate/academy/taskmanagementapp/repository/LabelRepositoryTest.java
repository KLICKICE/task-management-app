package mate.academy.taskmanagementapp.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import mate.academy.taskmanagementapp.model.label.Label;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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