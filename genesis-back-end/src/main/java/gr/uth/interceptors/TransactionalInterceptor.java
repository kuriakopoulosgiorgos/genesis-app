package gr.uth.interceptors;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Transactional
@Priority(1)
@Interceptor
public class TransactionalInterceptor {

    @AroundInvoke
    Object executeInTransaction(InvocationContext context) throws Exception {
        Class clazz = context.getMethod().getReturnType();
        if(clazz == Uni.class) {
            try {
                return Panache.withTransaction(() -> {
                    try {
                        return (Uni) context.proceed();
                    } catch (Exception e) {
                        ExceptionWrapper exceptionWrapper = new ExceptionWrapper();
                        exceptionWrapper.e = e;
                        throw exceptionWrapper;
                    }
                });
            } catch (ExceptionWrapper exceptionWrapper) {
                throw exceptionWrapper.e;
            }

        }
        return context.proceed();
    }

    private static class ExceptionWrapper extends RuntimeException {
        Exception e;
    }
}
