package mate.academy.taskmanagementapp.service.dropbox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class DropboxServiceImpl implements DropboxService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${dropbox.access.token}")
    private String accessToken;

    @Value("${dropbox.upload.url}")
    private String uploadUrl;

    @Value("${dropbox.download.url}")
    private String downloadUrl;

    @Value("${dropbox.delete.url}")
    private String deleteUrl;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String dropboxPath = "/" + file.getOriginalFilename();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization",
                    "Bearer " + accessToken);
            headers.add("Dropbox-API-Arg",
                    "{\"path\": \""
                            + dropboxPath
                            + "\",\"mode\": \"add\",\"autorename\": true,\"mute\": false}");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);
            ResponseEntity<Map> response = restTemplate
                    .exchange(uploadUrl, HttpMethod.POST, requestEntity, Map.class);

            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("id")) {
                log.info("File uploaded to Dropbox: {}", body.get("path_display"));
                return (String) body.get("id");
            } else {
                throw new RuntimeException("Dropbox upload failed: empty response body");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Dropbox", e);
        }
    }

    @Override
    public byte[] downloadFile(String dropboxFilePath) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Dropbox-API-Arg", "{\"path\": \"" + dropboxFilePath + "\"}");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> response = restTemplate
                .exchange(downloadUrl, HttpMethod.POST, requestEntity, byte[].class);
        return response.getBody();
    }

    @Override
    public void deleteFile(String dropboxFilePath) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> request = new HashMap<>();
        request.put("path", dropboxFilePath);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<Map> response = restTemplate
                .postForEntity(deleteUrl, requestEntity, Map.class);

        log.info("File deleted from Dropbox: {}", dropboxFilePath);
    }
}
