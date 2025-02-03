package test;

import gr.uth.configurations.JsonbFactory;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;
import jakarta.validation.*;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import jakarta.json.bind.Jsonb;
import org.json.JSONException;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class BaseTest {


    private final Jsonb objectMapper;
    private final Validator validator;

    public BaseTest() {
        var jsonbFactory = new JsonbFactory();
        this.objectMapper = jsonbFactory.jsonb(jsonbFactory.jsonbConfig().withFormatting(true));
        try (ValidatorFactory validatorFactory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()) {
            validator = validatorFactory.getValidator();
        }
    }

    public <T> T fromJson(String path, Type type) {
        try {
            String json = readFile(path);
            T obj = objectMapper.fromJson(json, type);
            Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
            if (!constraintViolations.isEmpty()) {
                System.out.println("Invalid json at path: " + path + " for type " + type.getTypeName());
                System.out.println("Constraints violated are:");
                constraintViolations.forEach(System.out::println);
                throw new ConstraintViolationException(constraintViolations);
            }
            return obj;
        } catch (JsonbException e) {
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
                .toArray(Customization[]::new);

        assertEquals(expectedJsonPath, actual,
                new CustomComparator(JSONCompareMode.STRICT, customizations));
    }

    private void assertEquals(String expectedJsonPath, Object actual, CustomComparator customComparator) {
        try {
            System.out.println("Expected Json Path : " + expectedJsonPath);
            String actualJson = objectMapper.toJson(actual);
            System.out.println("=== Actual ===");
            System.out.println(actualJson);
            Set<ConstraintViolation<Object>> constraintViolations = validator.validate(actual);
            if (!constraintViolations.isEmpty()) {
                System.out.println("The actual object is invalid");
                System.out.println("Constraints violated are:");
                constraintViolations.forEach(System.out::println);
                throw new AssertionError("Invalid actual object for class: " + actual.getClass().getName());
            }
            JSONAssert.assertEquals(Objects.requireNonNull(readFile(expectedJsonPath)), actualJson, customComparator);
        } catch (JSONException e) {
            System.out.println("Invalid json data");
            e.printStackTrace();
        }
    }

    private String readFile(String path) {
        try {
            var uri = Objects.requireNonNull(
                            getClass().getClassLoader().getResource(path),
                            "Invalid path: " + path)
                    .toURI();
            return new String(Files.readAllBytes(Paths.get(uri)));
        } catch (IOException | URISyntaxException e) {
            System.out.println("Couldn't read file from path : " + path);
            e.printStackTrace();
            return null;
        }
    }
}
