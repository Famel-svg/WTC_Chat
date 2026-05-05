package br.com.fiap.wtccrm.persistence.repository;

import br.com.fiap.wtccrm.persistence.document.AuditLogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditLogRepository extends MongoRepository<AuditLogDocument, String> {}
