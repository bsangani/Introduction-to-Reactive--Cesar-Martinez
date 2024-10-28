package org.gridu.mycapstoneproject;

import org.slf4j.MDC;
import reactor.core.publisher.Signal;

import java.util.Optional;
import java.util.function.Consumer;

public class LogginUtil {

    public static <T> Consumer<Signal<T>> logOnNext(Consumer<T> logStatement) {
        return signal -> {
            if (!signal.isOnNext()) return;
            Optional<String> toPutInMdc = signal.getContextView().getOrEmpty("requestId");

            toPutInMdc.ifPresentOrElse(tpim -> {
                        try (MDC.MDCCloseable cMdc = MDC.putCloseable("requestId", tpim)) {
                            logStatement.accept(signal.get());
                        }
                    },
                    () -> logStatement.accept(signal.get()));
        };
    }

    public static Consumer<Signal<?>> logOnError(Consumer<Throwable> errorLogStatement) {
        return signal -> {
            if (!signal.isOnError()) return;
            Optional<String> toPutInMdc = signal.getContextView().getOrEmpty("CONTEXT_KEY");

            toPutInMdc.ifPresentOrElse(tpim -> {
                        try (MDC.MDCCloseable cMdc = MDC.putCloseable("MDC_KEY", tpim)) {
                            errorLogStatement.accept(signal.getThrowable());
                        }
                    },
                    () -> errorLogStatement.accept(signal.getThrowable()));
        };
    }
}
