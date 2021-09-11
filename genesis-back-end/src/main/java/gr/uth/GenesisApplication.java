package gr.uth;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath(GenesisApplication.REST_PATH)
@OpenAPIDefinition(
        info = @Info(
                title="Genesis API",
                description = "A modern API for the new e-marketing era!",
                version = "1.0"
        )
)
public class GenesisApplication extends Application {

    public static final String REST_PATH = "/api";
}
