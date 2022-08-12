package com.bank.es;

import com.bank.es.exceptions.InvalidEventException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class AggregateRoot {

    protected String id;
    protected String type;
    protected long version;
    protected final List<Event> changes = new ArrayList<>();

    protected AggregateRoot(final String id, final String aggregateType) {
        this.id = id;
        this.type = aggregateType;
    }

    public abstract void when(final Event event);

    public void load(final List<Event> events) {
        events.forEach(
                event -> {
                    this.validateEvent(event);
                    this.raiseEvent(event);
                    this.version++;
                });
    }

    public void apply(final Event event) {
        this.validateEvent(event);
        event.setAggregateType(this.type);

        when(event);
        changes.add(event);

        this.version++;
        event.setVersion(this.version);
    }

    public void raiseEvent(final Event event) {
        this.validateEvent(event);

        event.setAggregateType(this.type);
        when(event);

        this.version++;
    }

    public void cleanChanges() {
        this.changes.clear();
    }

    public void toSnapshot() {
        this.cleanChanges();
    }

    private void validateEvent(final Event event) {
        if (Objects.isNull(event) || !event.getAggregateId().equals(this.id))
            throw new InvalidEventException(event.toString());
    }

    protected Event createEvent(String eventType, byte[] data, byte[] metadata) {
        return Event.builder()
                .aggregateId(this.getId())
                .version(this.getVersion())
                .aggregateType(this.getType())
                .eventType(eventType)
                .data(Objects.isNull(data) ? new byte[] {} : data)
                .metaData(Objects.isNull(metadata) ? new byte[] {} : data)
                .timeStamp(LocalDateTime.now())
                .build();
    }

    @Override
    public String toString() {
        return "AggregateRoot{"
                + "id='"
                + id
                + '\''
                + ", type='"
                + type
                + '\''
                + ", version="
                + version
                + ", changes="
                + changes.size()
                + '}';
    }
}
