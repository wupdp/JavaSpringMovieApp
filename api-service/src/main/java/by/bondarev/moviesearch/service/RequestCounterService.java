package by.bondarev.moviesearch.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class RequestCounterService {
    private final Counter requestCounter;

    public RequestCounterService(MeterRegistry meterRegistry) {
        this.requestCounter = Counter.builder("requests.api.total")
                .description("Total number of api-requests")
                .register(meterRegistry);
    }

    public synchronized void incrementRequestCounter() {
            requestCounter.increment();
    }
}
