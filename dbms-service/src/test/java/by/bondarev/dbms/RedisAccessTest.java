package by.bondarev.dbms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@DataRedisTest
public class RedisAccessTest {

    @Container
    private static final DockerComposeContainer<?> dockerComposeContainer =
            new DockerComposeContainer<>(new File("../test-docker-compose.yml"))
                    .withExposedService("redis", 6379, Wait.forListeningPort());

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void testRedisWriteAndRead() {
        redisTemplate.opsForValue().set("testKey", "testValue");

        String value = redisTemplate.opsForValue().get("testKey");

        assertEquals("testValue", value);
    }

    @Test
    public void testRedisDelete() {
        // Записываем значение в Redis
        redisTemplate.opsForValue().set("testKey", "testValue");

        // Удаляем значение из Redis
        redisTemplate.delete("testKey");

        // Пытаемся прочитать удаленное значение из Redis
        String value = redisTemplate.opsForValue().get("testKey");

        // Проверяем, что значение удалено и возвращается null
        assertEquals(null, value);
    }
}
