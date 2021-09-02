package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gr.uth.configurations.ObjectMapperCustomizerConfiguration;
import io.restassured.path.json.mapper.factory.DefaultJackson2ObjectMapperFactory;
import org.json.JSONException;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BaseTest {

    private ObjectMapper objectMapper;

    public BaseTest() {
        this.objectMapper = new DefaultJackson2ObjectMapperFactory().create(null, null);
        ObjectMapperCustomizerConfiguration objectMapperCustomizerConfiguration = new ObjectMapperCustomizerConfiguration();
        objectMapperCustomizerConfiguration.customize(objectMapper);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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
        assertEquals(expectedJsonPath, actual, Collections.emptyList());
    }

    protected void assertEquals(String expectedJsonPath, Object actual, List<String> ignoreFields) {
        var customizations = ignoreFields.stream()
                .map(ignoreField -> new Customization(ignoreField, (o1, o2) -> true))
                .collect(Collectors.toList());

        assertEquals(expectedJsonPath, actual,
                new CustomComparator(JSONCompareMode.STRICT, customizations.toArray(new Customization[0])));
    }

    private void assertEquals(String expectedJsonPath, Object actual, CustomComparator customComparator) {
        try {
            System.out.println("Expected Json Path : " + expectedJsonPath);
            String actualJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(actual);
            System.out.println("=== Actual ===");
            System.out.println(actualJson);
            JSONAssert.assertEquals(readFile(expectedJsonPath), actualJson, customComparator);
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
