package gr.uth.resources;

import gr.uth.dto.ApplicationConfiguration;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path("/configuration")
@Tag(name = "configuration")
public class ConfigurationResource {

    @Inject
    ApplicationConfiguration applicationConfiguration;

    public ConfigurationResource(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public Uni<Map<String, String>> retrieveConfiguration() {
        return Uni.createFrom().item(applicationConfiguration.frontend());
    }
}
