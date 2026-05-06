package br.com.fiap.wtccrm.persistence.repository;

import com.google.cloud.firestore.Firestore;

import br.com.fiap.wtccrm.persistence.document.MessageDocument;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepository extends FirestoreCollectionRepository<MessageDocument> {

    public MessageRepository(Firestore firestore) {
        super("messages", MessageDocument.class, firestore);
    }

    public List<MessageDocument> findByConversationIdOrderBySentAtAsc(String conversationId) {
        return findByField("conversationId", conversationId).stream()
                .sorted(Comparator.comparing(MessageDocument::getSentAt, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }
}


