package gr.uth.models;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class Model {

    @NotBlank
    public String fileReference;
    @NotBlank
    public String name;
    public String description;
    public LocalDateTime uploadDate;
}
