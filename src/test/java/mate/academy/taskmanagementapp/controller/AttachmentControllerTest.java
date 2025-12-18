package mate.academy.taskmanagementapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import mate.academy.taskmanagementapp.dto.attachment.AttachmentDto;
import mate.academy.taskmanagementapp.service.dropbox.DropboxService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AttachmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DropboxService dropboxService;

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(
            scripts = {
                    "/testData/clean.sql",
                    "/testData/insert_roles.sql",
                    "/testData/insert_users.sql",
                    "/testData/insert_project_statuses.sql",
                    "/testData/projects.sql",
                    "/testData/insert_task.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Add attachment successfully")
    void addAttachment_success() throws Exception {
        Mockito.when(dropboxService.uploadFile(any()))
                .thenReturn("dbx-file-1");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "hello".getBytes()
        );

        MvcResult mvcResult = mockMvc.perform(multipart("/attachments")
                        .file(file)
                        .param("taskId", "1"))
                .andExpect(status().isCreated())
                .andReturn();

        AttachmentDto dto = fromJson(mvcResult, AttachmentDto.class);
        assertNotNull(dto);
        assertEquals(1L, dto.getTaskId());
        assertEquals("test.txt", dto.getFileName());
        assertEquals("dbx-file-1", dto.getDropboxFileId());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(
            scripts = {
                    "/testData/clean.sql",
                    "/testData/insert_roles.sql",
                    "/testData/insert_users.sql",
                    "/testData/insert_project_statuses.sql",
                    "/testData/projects.sql",
                    "/testData/insert_task.sql",
                    "/testData/insert_attachment.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Get attachments by taskId successfully")
    void getAttachmentsByTaskId_success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/attachments")
                        .param("taskId", "1"))
                .andExpect(status().isOk())
                .andReturn();

        AttachmentDto[] attachments = fromJson(mvcResult, AttachmentDto[].class);

        assertNotNull(attachments);
        assertTrue(attachments.length > 0);
        assertEquals(1L, attachments[0].getTaskId());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(
            scripts = {
                    "/testData/clean.sql",
                    "/testData/insert_roles.sql",
                    "/testData/insert_users.sql",
                    "/testData/insert_project_statuses.sql",
                    "/testData/projects.sql",
                    "/testData/insert_task.sql",
                    "/testData/insert_attachment.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Download attachment successfully")
    void downloadAttachment_success() throws Exception {
        byte[] bytes = "file-content".getBytes();

        Mockito.when(dropboxService.downloadFile("dbx-file-1"))
                .thenReturn(bytes);

        MvcResult mvcResult = mockMvc.perform(get("/attachments/1/download"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("application/octet-stream", mvcResult.getResponse().getContentType());
        String cd = mvcResult.getResponse().getHeader("Content-Disposition");
        assertNotNull(cd);
        assertTrue(cd.contains("attachment"));
        assertTrue(cd.contains("test.txt"));

        assertEquals(bytes.length, mvcResult.getResponse().getContentAsByteArray().length);
        assertEquals("file-content", new String(mvcResult.getResponse().getContentAsByteArray()));
    }

    private <T> T fromJson(MvcResult result, Class<T> clazz) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsString(), clazz);
    }
}
