package br.com.fiap.wtccrm.persistence.repository;

import com.google.cloud.firestore.Firestore;

import br.com.fiap.wtccrm.persistence.document.AuditLogDocument;
import org.springframework.stereotype.Repository;

@Repository
public class AuditLogRepository extends FirestoreCollectionRepository<AuditLogDocument> {

    public AuditLogRepository(Firestore firestore) {
        super("audit_logs", AuditLogDocument.class, firestore);
    }
}


