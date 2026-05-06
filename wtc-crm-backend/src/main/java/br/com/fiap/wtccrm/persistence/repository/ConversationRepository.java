package br.com.fiap.wtccrm.persistence.repository;

import com.google.cloud.firestore.Firestore;

import br.com.fiap.wtccrm.persistence.document.ConversationDocument;
import org.springframework.stereotype.Repository;

@Repository
public class ConversationRepository extends FirestoreCollectionRepository<ConversationDocument> {

    public ConversationRepository(Firestore firestore) {
        super("conversations", ConversationDocument.class, firestore);
    }
}


