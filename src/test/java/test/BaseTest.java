package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import gr.uth.configurations.ObjectMapperCustomizerConfiguration;
import io.quarkus.mongodb.panache.jackson.ObjectIdDeserializer;
import io.quarkus.mongodb.panache.jackson.ObjectIdSerializer;
import io.restassured.path.json.mapper.factory.DefaultJackson2ObjectMapperFactory;
import org.bson.types.ObjectId;
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
        SimpleModule simpleModule = new SimpleModule("ObjectIdSerializer", new Version(1, 0, 0, null, null, null));
        simpleModule.addSerializer(ObjectId.class, new ObjectIdSerializer());
        simpleModule.addDeserializer(ObjectId.class, new ObjectIdDeserializer());
        this.objectMapper.registerModule(simpleModule);
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
