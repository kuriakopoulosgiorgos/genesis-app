package gr.uth.services;

import com.stripe.exception.StripeException;

import java.util.Map;

public interface CheckoutService {

    /**
     * Create a new checkout session
     * @param cart the shopping cart
     * @param referer the referer of the request, the redirect url will be based on it
     * @param paymentSuccessFragment the url fragment to handle the payment success
     * @param paymentCancelFragment the url fragment to handle the payment cancel
     * @return the redirect url
     */
    String checkout(Map<String, Integer> cart,
                    String referer,
                    String paymentSuccessFragment,
                    String paymentCancelFragment)
            throws StripeException;
}
