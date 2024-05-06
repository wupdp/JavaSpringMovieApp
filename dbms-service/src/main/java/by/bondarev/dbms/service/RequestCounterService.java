package by.bondarev.dbms.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class RequestCounterService {
    private final Counter requestCounter;

    public RequestCounterService(MeterRegistry meterRegistry) {
        this.requestCounter = Counter.builder("requests.dbms.total")
                .description("Total number of dbms-requests")
                .register(meterRegistry);
    }

    public synchronized void incrementRequestCounter() {
            requestCounter.increment();
    }
}
