package gr.uth.resources;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Path("/configuration")
@Tag(name = "configuration")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConfigurationResource {

    private static final String FRONTEND_CONFIGURATION = "application.configuration.frontend";

    @Inject
    private Config config;

    /**
     * @return A json object containing all fronted configuration properties
     */
    @GET
    public JsonObject retrieveConfiguration() {
        return Json.createObjectBuilder(
                StreamSupport.stream(config.getPropertyNames().spliterator(), false)
                        .filter(propertyName -> propertyName.contains(FRONTEND_CONFIGURATION))
                        .collect(Collectors.toMap(Function.identity(), propertyName -> config.getValue(propertyName, String.class)))
        ).build();
    }
}
