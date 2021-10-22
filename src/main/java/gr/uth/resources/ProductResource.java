package gr.uth.resources;

import gr.uth.dto.Pageable;
import gr.uth.dto.ProductSortByField;
import gr.uth.dto.SortByDirection;
import gr.uth.models.Product;
import gr.uth.services.ProductService;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/products")
@Tag(name = "Product")
public class ProductResource {

    @Inject
    ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public Uni<Pageable<Product>> findAll(
            @QueryParam("sortByField") ProductSortByField sortByField,
            @QueryParam("sortByDirection") @DefaultValue("asc") SortByDirection sortByDirection,
            @QueryParam("page") @DefaultValue("1") @Min(1) int page,
            @QueryParam("pageSize") @DefaultValue("25") @Min(1) @Max(50) int pageSize) {

        return productService.findAll(sortByField, sortByDirection, page, pageSize);
    }

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Uni<Product> create(@Valid Product product) {
        return productService.create(product);
    }

    @GET
    @Path(value = "/{id}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Uni<Product> findById(Long id) {
        return productService.findById(id);
    }

    @DELETE
    @Path(value = "/{id}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.TEXT_PLAIN)
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Product deleted",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "No Product found",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN)
            ),
    })
    public Uni<Response> deleteById(Long id) {

        return productService.deleteById(id).map(isDeleted ->
                        isDeleted ? Response.status(Response.Status.NO_CONTENT).build()
                                : Response.status(Response.Status.NOT_FOUND).build());
    }
}
