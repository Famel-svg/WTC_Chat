package br.com.fiap.wtccrm.persistence.repository;

import br.com.fiap.wtccrm.persistence.document.ClientDocument;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<ClientDocument, String> {

    List<ClientDocument> findBySegmentId(String segmentId);

    long countBySegmentId(String segmentId);
}
