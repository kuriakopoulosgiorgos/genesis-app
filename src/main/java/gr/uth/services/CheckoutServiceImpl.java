package gr.uth.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import gr.uth.models.Product;
import gr.uth.repositories.ProductRepository;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class CheckoutServiceImpl implements CheckoutService {

    @Inject
    @ConfigProperty(name = "stripe.api.secret-key")
    private String stripeApiSecretKey;
    private ProductRepository productRepository;

    public CheckoutServiceImpl() {
    }

    @Inject
    public CheckoutServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostConstruct
    void init() {
        Stripe.apiKey = stripeApiSecretKey;
    }

    @Override
    public String checkout(Map<String, Integer> cart, String referer,
                           String paymentSuccessFragment, String paymentCancelFragment) throws StripeException {
        List<Long> productIds = cart.keySet().stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        List<Product> products = productRepository.findByIds(productIds);

        List<SessionCreateParams.LineItem> lineItems = products.stream().map(product ->
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("eur")
                                        .setUnitAmount((long) ((product.getPrice() * cart.get("" + product.getId())) * 100))
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(product.getName())
                                                        .build()
                                        )
                                        .build())
                        .build()
        ).collect(Collectors.toList());

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        // The Billing address information will be required to be able to make a payment
                        .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                        .setPaymentIntentData(
                                SessionCreateParams.PaymentIntentData.builder()
                                        // We will be capturing the amount manually in order to validate products stock
                                        .setCaptureMethod(SessionCreateParams.PaymentIntentData.CaptureMethod.MANUAL)
                                        .build())
                        .setSuccessUrl(referer + paymentSuccessFragment)
                        .setCancelUrl(referer + paymentCancelFragment)
                        .addAllLineItem(lineItems)
                        .build();

        Session session = Session.create(params);
        return session.getUrl();
    }
}
