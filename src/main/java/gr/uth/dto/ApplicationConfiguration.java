package gr.uth.dto;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;

import java.util.Map;

@StaticInitSafe
@ConfigMapping(prefix = "application.configuration")
public interface ApplicationConfiguration {

    Map<String, String> frontend();
}
