package br.com.fiap.wtccrm.audit.service;

import br.com.fiap.wtccrm.audit.model.AuditLog;
import br.com.fiap.wtccrm.persistence.document.AuditLogDocument;
import br.com.fiap.wtccrm.persistence.repository.AuditLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Async
    public void logAction(AuditLog log) {
        AuditLogDocument doc = new AuditLogDocument();
        doc.setId(log.id());
        doc.setOperatorId(log.operatorId());
        doc.setAction(log.action());
        doc.setEntityType(log.entityType());
        doc.setEntityId(log.entityId());
        doc.setDescription(log.description());
        doc.setIpAddress(log.ipAddress());
        doc.setTimestamp(log.timestamp());
        auditLogRepository.save(doc);
        LOGGER.debug("Audit persisted id={}", log.id());
    }
}
