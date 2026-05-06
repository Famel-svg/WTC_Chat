package br.com.fiap.wtccrm.persistence.repository;

import com.google.cloud.firestore.Firestore;

import br.com.fiap.wtccrm.persistence.document.CampaignDocument;
import org.springframework.stereotype.Repository;

@Repository
public class CampaignRepository extends FirestoreCollectionRepository<CampaignDocument> {

    public CampaignRepository(Firestore firestore) {
        super("campaigns", CampaignDocument.class, firestore);
    }
}


