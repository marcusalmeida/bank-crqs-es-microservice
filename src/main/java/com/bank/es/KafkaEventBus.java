package com.bank.es;

import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaEventBus implements EventBus {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private static final long sendTimeout = 3000;

    @Value(value = "${order.kafka.topics.bank-account-event-store:bank-account-event-store}")
    private String bankAccountTopicName;

    @Override
    @NewSpan
    public void publish(@SpanTag("events") List<Event> events) {
        final byte[] eventsBytes =
                SerializerUtils.serializeToJsonBytes(events.toArray(new Event[] {}));
        final ProducerRecord<String, byte[]> _record =
                new ProducerRecord<>(bankAccountTopicName, eventsBytes);

        try {
            kafkaTemplate.send(_record).get(sendTimeout, TimeUnit.MILLISECONDS);
            log.info("publish kafka record value >>>>> {}", new String(_record.value()));
        } catch (Exception ex) {
            log.error("(KafkaEventBus) publish get timeout", ex);
            throw new RuntimeException(ex);
        }
    }
}
