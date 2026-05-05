package br.com.fiap.wtccrm.persistence.repository;

import br.com.fiap.wtccrm.persistence.document.ConversationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConversationRepository extends MongoRepository<ConversationDocument, String> {}
