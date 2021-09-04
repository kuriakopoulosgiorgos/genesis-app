package gr.uth;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath(GenesisApplication.REST_PATH)
public class GenesisApplication extends Application {

    public static final String REST_PATH = "/api";

    @PostConstruct
    public void onInit() {
    }
}
