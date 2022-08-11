package org.acme.cashback;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.reactive.messaging.kafka.IncomingKafkaRecord;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ExpenseMessageSource {

    private static final Logger log = LoggerFactory.getLogger(ExpenseMessageSource.class);

    @Inject
    CashbackService cashbackService;

    @Incoming("expense")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    public Uni<Void> processMessage(IncomingKafkaRecord<String, String> message) {
        return Uni.createFrom().item(message).emitOn(Infrastructure.getDefaultWorkerPool()).onItem()
                .transform(r -> {
                    log.debug("Consumed message with payload " + message.getPayload());
                    return new JsonObject(message.getPayload());
                })
                .onItem().transform(json -> {
                    doProcessMessage(json);
                    return null;
                });
    }

    private void doProcessMessage(JsonObject expenseEvent) {
        cashbackService.processEventMessage(expenseEvent.getLong("sale_id"), expenseEvent.getString("op"));
    }
}
