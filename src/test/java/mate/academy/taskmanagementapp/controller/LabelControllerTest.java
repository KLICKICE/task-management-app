package mate.academy.taskmanagementapp.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.taskmanagementapp.dto.label.CreateLabelRequestDto;
import mate.academy.taskmanagementapp.dto.label.LabelDto;
import mate.academy.taskmanagementapp.dto.label.UpdateLabelDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LabelControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "/testData/clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Create a new label successfully")
    void createLabel_ValidRequestDto_success() throws Exception {
        CreateLabelRequestDto request = createLabelRequestDto("Important");

        MvcResult result = mockMvc.perform(post("/labels")
                        .content(toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        LabelDto actual = fromJson(result, LabelDto.class);

        assertNotNull(actual);
        assertEquals("Important", actual.getName());
        assertEquals("red", actual.getColor());
        assertNotNull(actual.getId());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "/testData/label.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Delete existing label by id successfully")
    void deleteLabelById_ValidId_success() throws Exception {
        mockMvc.perform(delete("/labels/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @Sql(scripts = "/testData/label.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Get all labels successfully")
    void getAllLabels_success() throws Exception {
        MvcResult result = mockMvc.perform(get("/labels")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        LabelDto[] labels = objectMapper.readValue(jsonResponse, LabelDto[].class);

        assertEquals(3, labels.length);
        assertEquals("Important", labels[0].getName());
        assertEquals("black", labels[0].getColor());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "/testData/label.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Update existing label successfully")
    void updateLabel_ValidRequestDto_success() throws Exception {
        UpdateLabelDto updateDto = new UpdateLabelDto();
        updateDto.setName("Updated Label");
        updateDto.setColor("green");

        MvcResult result = mockMvc.perform(put("/labels/{id}", 1)
                        .content(toJson(updateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        LabelDto actual = fromJson(result, LabelDto.class);

        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals("Updated Label", actual.getName());
        assertEquals("green", actual.getColor());
    }

    private CreateLabelRequestDto createLabelRequestDto(String name) {
        CreateLabelRequestDto dto = new CreateLabelRequestDto();
        dto.setColor("red");
        dto.setName(name);
        return dto;
    }

    private String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    private <T> T fromJson(MvcResult result, Class<T> clazz) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsString(), clazz);
    }
}
