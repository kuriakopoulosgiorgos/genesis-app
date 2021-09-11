package gr.uth.interceptors;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@InterceptorBinding
public @interface Transactional {
}
