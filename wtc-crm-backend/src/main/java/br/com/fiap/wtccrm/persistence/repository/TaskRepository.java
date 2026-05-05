package br.com.fiap.wtccrm.persistence.repository;

import br.com.fiap.wtccrm.persistence.document.TaskDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<TaskDocument, String> {}
