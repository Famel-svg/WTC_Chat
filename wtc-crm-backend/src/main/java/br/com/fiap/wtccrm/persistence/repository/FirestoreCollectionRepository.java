package br.com.fiap.wtccrm.persistence.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;

abstract class FirestoreCollectionRepository<T> {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

    private final String collectionName;
    private final Class<T> documentType;
    private final Firestore firestore;

    protected FirestoreCollectionRepository(String collectionName, Class<T> documentType, Firestore firestore) {
        this.collectionName = collectionName;
        this.documentType = documentType;
        this.firestore = firestore;
    }

    public T save(T document) {
        String id = extractId(document);
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Firestore document id is required");
        }
        await(db().collection(collectionName).document(id).set(toMap(document)));
        return document;
    }

    public List<T> findAll() {
        return await(db().collection(collectionName).get()).getDocuments().stream()
                .map(this::fromSnapshot)
                .toList();
    }

    public Optional<T> findById(String id) {
        DocumentSnapshot snapshot = await(db().collection(collectionName).document(id).get());
        if (!snapshot.exists()) {
            return Optional.empty();
        }
        return Optional.of(fromSnapshot(snapshot));
    }

    public void deleteById(String id) {
        await(db().collection(collectionName).document(id).delete());
    }

    protected List<T> findByField(String fieldName, Object value) {
        return await(db().collection(collectionName).whereEqualTo(fieldName, value).get()).getDocuments().stream()
                .map(this::fromSnapshot)
                .toList();
    }

    private Firestore db() {
        return firestore;
    }

    private T fromSnapshot(DocumentSnapshot snapshot) {
        Map<String, Object> data = snapshot.getData();
        if (data == null) {
            data = Map.of();
        }
        return MAPPER.convertValue(data, documentType);
    }

    private Map<String, Object> toMap(T document) {
        return MAPPER.convertValue(document, MAP_TYPE);
    }

    private String extractId(T document) {
        try {
            Method getter = document.getClass().getMethod("getId");
            Object value = getter.invoke(document);
            return value == null ? null : value.toString();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Document type must expose getId()", e);
        }
    }

    private <R> R await(com.google.api.core.ApiFuture<R> future) {
        try {
            return future.get();
        } catch (Exception e) {
            Throwable root = rootCause(e);
            throw new IllegalStateException("Firestore operation failed: " + root.getMessage(), e);
        }
    }

    private Throwable rootCause(Throwable throwable) {
        Throwable current = throwable;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        return current;
    }
}

