package br.com.fiap.wtccrm.persistence.repository;

import com.google.cloud.firestore.Firestore;

import br.com.fiap.wtccrm.persistence.document.UserDocument;
import java.util.Locale;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends FirestoreCollectionRepository<UserDocument> {

    public UserRepository(Firestore firestore) {
        super("users", UserDocument.class, firestore);
    }

    public Optional<UserDocument> findByEmailIgnoreCase(String email) {
        if (email == null) {
            return Optional.empty();
        }
        String normalized = email.toLowerCase(Locale.ROOT);
        return findAll().stream()
                .filter(user -> user.getEmail() != null && user.getEmail().toLowerCase(Locale.ROOT).equals(normalized))
                .findFirst();
    }

    public boolean existsByEmailIgnoreCase(String email) {
        return findByEmailIgnoreCase(email).isPresent();
    }
}


