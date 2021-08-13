package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gr.uth.configurations.ObjectMapperCustomizerConfiguration;
import io.restassured.path.json.mapper.factory.DefaultJackson2ObjectMapperFactory;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BaseTest {

    private ObjectMapper objectMapper;

    public BaseTest() {
        this.objectMapper = new DefaultJackson2ObjectMapperFactory().create(null, null);
        ObjectMapperCustomizerConfiguration objectMapperCustomizerConfiguration = new ObjectMapperCustomizerConfiguration();
        objectMapperCustomizerConfiguration.customize(objectMapper);
    }

    public <T> T fromJson(String path, Class<T> clazz) {
        try {
            String json = readFile(path);
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            System.out.println("Couldn't read file from path : " + path);
            e.printStackTrace();
            return null;
        }
    }

    protected void assertEquals(String expectedJsonPath, Object actual) {
        try {
            String actualJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(actual);
            System.out.println("=== Actual ===");
            System.out.println(actualJson);
            JSONAssert.assertEquals(readFile(expectedJsonPath), actualJson, true);
        } catch (JSONException | JsonProcessingException e) {
            System.out.println("Invalid json data");
            e.printStackTrace();
        }
    }

    private String readFile(String path) {
        try {
            var uri = getClass().getClassLoader().getResource(path).toURI();
            return new String(Files.readAllBytes(Paths.get(uri)));
        } catch (IOException | URISyntaxException e) {
            System.out.println("Couldn't read file from path : " + path);
            e.printStackTrace();
            return null;
        }
    }
}
