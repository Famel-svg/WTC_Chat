package br.com.fiap.wtccrm.persistence.repository;

import com.google.cloud.firestore.Firestore;

import br.com.fiap.wtccrm.persistence.document.SegmentDocument;
import org.springframework.stereotype.Repository;

@Repository
public class SegmentRepository extends FirestoreCollectionRepository<SegmentDocument> {

    public SegmentRepository(Firestore firestore) {
        super("segments", SegmentDocument.class, firestore);
    }
}


