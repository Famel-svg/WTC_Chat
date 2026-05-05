package br.com.fiap.wtccrm.persistence.repository;

import br.com.fiap.wtccrm.persistence.document.CampaignDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CampaignRepository extends MongoRepository<CampaignDocument, String> {}
