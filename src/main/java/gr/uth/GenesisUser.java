package gr.uth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

@RequestScoped
public class GenesisUser {

    @Inject
    JsonWebToken jwt;
    private String username;
    private List<String> roles;

    @PostConstruct
    public void init() {
        if(Objects.nonNull(jwt)) {
            this.username = jwt.getName();
            this.roles = Objects.nonNull(jwt.getGroups()) ? new ArrayList<>(jwt.getGroups()) : List.of();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
