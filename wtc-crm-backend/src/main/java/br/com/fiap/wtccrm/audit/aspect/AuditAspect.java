package br.com.fiap.wtccrm.audit.aspect;

import br.com.fiap.wtccrm.audit.model.AuditLog;
import br.com.fiap.wtccrm.audit.service.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.UUID;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditAspect {

    private final AuditService auditService;
    private final HttpServletRequest request;

    public AuditAspect(AuditService auditService, HttpServletRequest request) {
        this.auditService = auditService;
        this.request = request;
    }

    @AfterReturning("@annotation(auditable)")
    public void log(JoinPoint joinPoint, Auditable auditable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String operatorId = auth == null ? "anonymous" : String.valueOf(auth.getPrincipal());
        AuditLog log = new AuditLog(
                UUID.randomUUID().toString(),
                operatorId,
                auditable.action(),
                auditable.entityType(),
                joinPoint.getSignature().getName(),
                "Action executed by aspect",
                request.getRemoteAddr(),
                Instant.now());
        auditService.logAction(log);
    }
}
