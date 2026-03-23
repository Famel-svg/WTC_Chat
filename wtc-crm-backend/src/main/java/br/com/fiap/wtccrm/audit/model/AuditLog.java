package br.com.fiap.wtccrm.audit.model;

import java.time.Instant;

public record AuditLog(
        String id,
        String operatorId,
        String action,
        String entityType,
        String entityId,
        String description,
        String ipAddress,
        Instant timestamp) {
}
