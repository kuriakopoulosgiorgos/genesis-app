package gr.uth.configurations;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

@ApplicationScoped
public class JsonbFactory {

    @Produces
    public JsonbConfig jsonbConfig() {
        return new JsonbConfig().withNullValues(false);
    }

    @Produces
    public Jsonb jsonb(JsonbConfig jsonbConfig) {
        return JsonbBuilder.create(jsonbConfig);
    }
}
