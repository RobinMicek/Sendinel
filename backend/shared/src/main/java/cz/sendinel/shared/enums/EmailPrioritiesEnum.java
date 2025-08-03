package cz.sendinel.shared.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EmailPrioritiesEnum {
    LOWEST(0),
    LOW(1),
    NORMAL(2),
    HIGH(3),
    HIGHEST(4);

    private final int rabbitMQQueuePriority; // Indexing from zero

}
