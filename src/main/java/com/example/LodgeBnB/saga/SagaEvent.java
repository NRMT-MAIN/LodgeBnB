package com.example.LodgeBnB.saga;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SagaEvent {
    private String sagaId;

    private String eventType;

    private String step;

    private Map<String, Object> payload;

    private LocalDateTime timestamp;

    private String status;

    public enum SagaStatus {
        PENDING,
        COMPLETED,
        FAILED,
        COMPENSATING
    }
}
