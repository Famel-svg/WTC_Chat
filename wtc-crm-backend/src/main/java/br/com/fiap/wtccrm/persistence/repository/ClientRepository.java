package br.com.fiap.wtccrm.persistence.repository;

import com.google.cloud.firestore.Firestore;

import br.com.fiap.wtccrm.persistence.document.ClientDocument;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ClientRepository extends FirestoreCollectionRepository<ClientDocument> {

    public ClientRepository(Firestore firestore) {
        super("clients", ClientDocument.class, firestore);
    }

    public List<ClientDocument> findBySegmentId(String segmentId) {
        return findByField("segmentId", segmentId);
    }

    public long countBySegmentId(String segmentId) {
        return findBySegmentId(segmentId).size();
    }
}


