package br.com.fiap.wtccrm.persistence.repository;

import br.com.fiap.wtccrm.persistence.document.SegmentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SegmentRepository extends MongoRepository<SegmentDocument, String> {}
