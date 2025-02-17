package gr.uth.resources;

import gr.uth.dto.Pageable;
import gr.uth.dto.ProductSortByField;
import gr.uth.dto.SortByDirection;
import gr.uth.models.Product;
import gr.uth.services.ProductService;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/products")
@Tag(name = "Product")
public class ProductResource {

    @Inject
    private ProductService productService;

    public ProductResource() {
    }

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    public Pageable<Product> findAll(
            @QueryParam("sortByField") ProductSortByField sortByField,
            @QueryParam("sortByDirection") @DefaultValue("asc") SortByDirection sortByDirection,
            @QueryParam("page") @DefaultValue("1") @Min(1) int page,
            @QueryParam("pageSize") @DefaultValue("25") @Min(1) @Max(50) int pageSize,
            @QueryParam("productCodes") List<Long> productCodes,
            @QueryParam("searchTerm") String searchTerm) {

        return productService.findAll(sortByField, sortByDirection, page, pageSize, productCodes, searchTerm);
    }

    @POST
    @RolesAllowed("supplier")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Product create(@Valid Product product) {
        return productService.create(product);
    }

    @GET
    @Path(value = "/{id}")
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    public Product findById(@PathParam("id") Long id) {
        return productService.findById(id);
    }

    @DELETE
    @RolesAllowed("supplier")
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
    public Response deleteById(@PathParam("id") Long id) {
        return productService.deleteById(id)
                ? Response.status(Response.Status.NO_CONTENT).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}
