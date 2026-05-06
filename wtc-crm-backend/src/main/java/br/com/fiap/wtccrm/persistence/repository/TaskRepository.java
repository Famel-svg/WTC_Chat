package br.com.fiap.wtccrm.persistence.repository;

import com.google.cloud.firestore.Firestore;

import br.com.fiap.wtccrm.persistence.document.TaskDocument;
import org.springframework.stereotype.Repository;

@Repository
public class TaskRepository extends FirestoreCollectionRepository<TaskDocument> {

    public TaskRepository(Firestore firestore) {
        super("tasks", TaskDocument.class, firestore);
    }
}


