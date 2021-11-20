package gr.uth.resources;

import com.stripe.exception.StripeException;
import gr.uth.services.CheckoutService;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Map;

@Path("/checkout")
@Tag(name = "Checkout")
public class CheckoutResource {

    @Inject
    final CheckoutService checkoutService;

    public CheckoutResource(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.TEXT_PLAIN)
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Checkout session started",
                    headers = @Header(name = "location", description = "The redirect location"),
                    content = @Content(mediaType = MediaType.TEXT_PLAIN)
            )
    })
    public Response checkout(Map<String, Integer> cart,
                             @HeaderParam("Referer") String referer,
                             @HeaderParam("paymentSuccessFragment") String paymentSuccessFragment,
                             @HeaderParam("paymentCancelFragment") String paymentCancelFragment) throws StripeException {

        return Response.status(Response.Status.ACCEPTED)
                .location(URI.create(
                        checkoutService.checkout(cart, referer, paymentSuccessFragment, paymentCancelFragment)
                ))
                .build();
    }
}
