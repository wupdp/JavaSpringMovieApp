package by.bondarev.dbcontroller;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class ServiceIntegrationTest {

    @Value("${dbms.url}")
    private String dbmsServiceUrl;

    @Value("${controller.url}")
    private String controllerServiceUrl;

    @Value("${api.url}")
    private String apiServiceUrl;

    @Container
    private static final DockerComposeContainer<?> dockerComposeContainer =
            new DockerComposeContainer<>(new File("../test-docker-compose.yml"))
                    .withExposedService("dbms-service", 8080, Wait.forListeningPort())
                    .withExposedService("db-controller", 8088, Wait.forListeningPort())
                    .withExposedService("api-service", 8081, Wait.forListeningPort());

    @Test
    public void testDbmsServiceConnection() {
        testServiceConnection(dbmsServiceUrl);
    }

    @Test
    public void testApiServiceConnection() {
        testServiceConnection(apiServiceUrl);
    }

    private void testServiceConnection(String serviceUrl) {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(serviceUrl + "/actuator/health", String.class);
        assert response != null;
        assertTrue(response.contains("UP"), "Service at " + serviceUrl + " is not up and running");
    }

    @Test
    public void testDbmsHealthEndpoint() {
        String expectedJson = "{\"status\":\"UP\"}";
        testEndpoint(dbmsServiceUrl + "/actuator/health", expectedJson);
    }

    @Test
    public void testControllerEndpoint() {
        String expectedJson = "[\n" +
                "  {\n" +
                "    \"id\": 7,\n" +
                "    \"name\": \"Мстители\",\n" +
                "    \"description\": \"Локи, сводный брат Тора, возвращается, и в этот раз он не один. Земля оказывается на грани порабощения, и только лучшие из лучших могут спасти человечество. Глава международной организации Щ.И.Т. Ник Фьюри собирает выдающихся поборников справедливости и добра, чтобы отразить атаку. Под предводительством Капитана Америки Железный Человек, Тор, Невероятный Халк, Соколиный Глаз и Чёрная Вдова вступают в войну с захватчиком.\",\n" +
                "    \"type\": \"movie\",\n" +
                "    \"typeNumber\": 1,\n" +
                "    \"status\": null,\n" +
                "    \"statusId\": 16,\n" +
                "    \"persons\": [],\n" +
                "    \"genres\": [\n" +
                "      {\n" +
                "        \"id\": 5,\n" +
                "        \"name\": \"фантастика\",\n" +
                "        \"movies\": null\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 102,\n" +
                "        \"name\": \"боевик\",\n" +
                "        \"movies\": null\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 4,\n" +
                "        \"name\": \"приключения\",\n" +
                "        \"movies\": null\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 53,\n" +
                "        \"name\": \"фэнтези\",\n" +
                "        \"movies\": null\n" +
                "      }\n" +
                "    ],\n" +
                "    \"countries\": [\n" +
                "      {\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"США\",\n" +
                "        \"movies\": null\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]";
        testEndpoint(controllerServiceUrl + "/movie/info?title=Мстители", expectedJson);
    }

    @Test
    public void testDbmsEndpoint() {
        String expectedJson = "[\n" +
                "  {\n" +
                "    \"id\": 7,\n" +
                "    \"name\": \"Мстители\",\n" +
                "    \"description\": \"Локи, сводный брат Тора, возвращается, и в этот раз он не один. Земля оказывается на грани порабощения, и только лучшие из лучших могут спасти человечество. Глава международной организации Щ.И.Т. Ник Фьюри собирает выдающихся поборников справедливости и добра, чтобы отразить атаку. Под предводительством Капитана Америки Железный Человек, Тор, Невероятный Халк, Соколиный Глаз и Чёрная Вдова вступают в войну с захватчиком.\",\n" +
                "    \"type\": \"movie\",\n" +
                "    \"typeNumber\": 1,\n" +
                "    \"status\": null,\n" +
                "    \"statusId\": 16,\n" +
                "    \"persons\": [],\n" +
                "    \"genres\": [\n" +
                "      {\n" +
                "        \"id\": 5,\n" +
                "        \"name\": \"фантастика\",\n" +
                "        \"movies\": null\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 102,\n" +
                "        \"name\": \"боевик\",\n" +
                "        \"movies\": null\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 4,\n" +
                "        \"name\": \"приключения\",\n" +
                "        \"movies\": null\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 53,\n" +
                "        \"name\": \"фэнтези\",\n" +
                "        \"movies\": null\n" +
                "      }\n" +
                "    ],\n" +
                "    \"countries\": [\n" +
                "      {\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"США\",\n" +
                "        \"movies\": null\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]";
        testEndpoint(dbmsServiceUrl + "/movies/byTitle/Мстители", expectedJson);
    }

    @Test
    public void testApiEndpoint() {
        String expectedJson = "{\n" +
                "  \"id\": 0,\n" +
                "  \"name\": \"Мстители\",\n" +
                "  \"description\": \"Локи, сводный брат Тора, возвращается, и в этот раз он не один. Земля оказывается на грани порабощения, и только лучшие из лучших могут спасти человечество. Глава международной организации Щ.И.Т. Ник Фьюри собирает выдающихся поборников справедливости и добра, чтобы отразить атаку. Под предводительством Капитана Америки Железный Человек, Тор, Невероятный Халк, Соколиный Глаз и Чёрная Вдова вступают в войну с захватчиком.\",\n" +
                "  \"type\": \"movie\",\n" +
                "  \"typeNumber\": 0,\n" +
                "  \"status\": null,\n" +
                "  \"statusId\": 0,\n" +
                "  \"persons\": null,\n" +
                "  \"genres\": [\n" +
                "    {\n" +
                "      \"id\": null,\n" +
                "      \"name\": \"приключения\",\n" +
                "      \"movies\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": null,\n" +
                "      \"name\": \"фантастика\",\n" +
                "      \"movies\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": null,\n" +
                "      \"name\": \"фэнтези\",\n" +
                "      \"movies\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": null,\n" +
                "      \"name\": \"боевик\",\n" +
                "      \"movies\": null\n" +
                "    }\n" +
                "  ],\n" +
                "  \"countries\": [\n" +
                "    {\n" +
                "      \"id\": null,\n" +
                "      \"name\": \"США\",\n" +
                "      \"movies\": null\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        testEndpoint(apiServiceUrl + "/movie/info?title=Мстители", expectedJson);
    }

    @Test
    public void testApiHealthEndpoint() {
        String expectedJson = "{\"status\":\"UP\"}";
        testEndpoint(apiServiceUrl + "/actuator/health", expectedJson);
    }

    private void testEndpoint(String endpointUrl, String expectedJson) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(endpointUrl, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        String responseBody = response.getBody();
        assertNotNull(responseBody);

        try {
            JSONAssert.assertEquals(expectedJson, responseBody, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
