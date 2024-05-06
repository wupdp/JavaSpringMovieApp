package by.bondarev.dbms;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
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

    static String dbmsServiceUrl = "http://localhost:8080";

    @Value("${spring.datasource.url}")
    static String dbServiceUrl;
    @Value("${spring.datasource.username}")
    static String dbUsername;
    @Value("${spring.datasource.password}")
    static String dbPassword;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Container
    private static final DockerComposeContainer<?> dockerComposeContainer =
            new DockerComposeContainer<>(new File("../test-docker-compose.yml"))
                    .withExposedService("db-container", 5432, Wait.forListeningPort())
                    .withExposedService("dbms-service", 8080, Wait.forListeningPort());

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

    @Transactional
    @Rollback
    @Test
    public void testDatabaseConnection() {
        jdbcTemplate.execute("CREATE TABLE test_table (id SERIAL PRIMARY KEY, name VARCHAR(255))");
        jdbcTemplate.update("INSERT INTO test_table (name) VALUES (?)", "Test");

        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM test_table", Integer.class);
        assertEquals(1, count);

        jdbcTemplate.execute("DROP TABLE test_table");
    }
}
