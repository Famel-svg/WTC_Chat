package br.com.fiap.wtccrm.audit.service;

import br.com.fiap.wtccrm.audit.model.AuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);

    @Async
    public void logAction(AuditLog log) {
        LOGGER.info("Audit {}", log);
    }
}
