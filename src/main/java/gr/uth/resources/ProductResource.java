package gr.uth.resources;

import gr.uth.models.Product;
import gr.uth.services.ProductService;
import io.smallrye.mutiny.Uni;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/products")
public class ProductResource {

    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public Uni<List<Product>> findAll() {
        return productService.findAll();
    }

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Uni<Product> create(@Valid Product product) {
        return productService.create(product);
    }

    @GET
    @Path(value = "/{code}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Uni<Product> findById(String id) {
        return productService.findById(id);
    }

    @DELETE
    @Path(value = "/{id}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Uni<Response> deleteById(@NotBlank String id) {

        return productService.deleteById(id).map(isDeleted ->
                        isDeleted ? Response.status(Response.Status.NO_CONTENT).build()
                                : Response.status(Response.Status.NOT_FOUND).build());
    }
}
