package br.com.fiap.wtccrm.clients.service;

import br.com.fiap.wtccrm.clients.dto.ClientDTO;
import br.com.fiap.wtccrm.exception.BusinessException;
import br.com.fiap.wtccrm.persistence.document.ClientDocument;
import br.com.fiap.wtccrm.persistence.repository.ClientRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<ClientDTO> findAll() {
        return clientRepository.findAll().stream()
                .map(this::toDto)
                .sorted(Comparator.comparing(ClientDTO::createdAt))
                .toList();
    }

    public ClientDTO findById(String id) {
        return clientRepository.findById(id).map(this::toDto).orElseThrow(() -> new BusinessException("Client not found"));
    }

    public ClientDTO create(ClientDTO request) {
        String id = UUID.randomUUID().toString();
        Instant now = Instant.now();
        ClientDocument doc = new ClientDocument();
        doc.setId(id);
        doc.setName(request.name());
        doc.setEmail(request.email());
        doc.setPhone(request.phone());
        doc.setCategory(normalizeCategory(request.category()));
        doc.setScore(clampScore(request.score()));
        doc.setStatus(normalizeStatus(request.status()));
        doc.setSegmentId(request.segmentId());
        doc.setAvatarUrl(request.avatarUrl());
        doc.setCreatedAt(now);
        doc.setUpdatedAt(now);
        clientRepository.save(doc);
        return toDto(doc);
    }

    public ClientDTO update(String id, ClientDTO request) {
        ClientDocument current = clientRepository.findById(id).orElseThrow(() -> new BusinessException("Client not found"));
        current.setName(request.name() == null ? current.getName() : request.name());
        current.setEmail(request.email() == null ? current.getEmail() : request.email());
        current.setPhone(request.phone() == null ? current.getPhone() : request.phone());
        current.setCategory(request.category() == null ? current.getCategory() : normalizeCategory(request.category()));
        current.setScore(request.score() == 0 ? current.getScore() : clampScore(request.score()));
        current.setStatus(request.status() == null ? current.getStatus() : normalizeStatus(request.status()));
        current.setSegmentId(request.segmentId() == null ? current.getSegmentId() : request.segmentId());
        current.setAvatarUrl(request.avatarUrl() == null ? current.getAvatarUrl() : request.avatarUrl());
        current.setUpdatedAt(Instant.now());
        clientRepository.save(current);
        return toDto(current);
    }

    public void delete(String id) {
        findById(id);
        clientRepository.deleteById(id);
    }

    public List<ClientDTO> findBySegment(String segmentId) {
        return clientRepository.findBySegmentId(segmentId).stream().map(this::toDto).toList();
    }

    private int clampScore(int score) {
        return Math.max(0, Math.min(100, score));
    }

    private String normalizeCategory(String category) {
        if (category == null || category.isBlank()) {
            return "STANDARD";
        }
        return category.toUpperCase();
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) {
            return "ACTIVE";
        }
        return status.toUpperCase();
    }

    private ClientDTO toDto(ClientDocument d) {
        return new ClientDTO(
                d.getId(),
                d.getName(),
                d.getEmail(),
                d.getPhone(),
                d.getCategory(),
                d.getScore(),
                d.getStatus(),
                d.getSegmentId(),
                d.getAvatarUrl(),
                d.getCreatedAt(),
                d.getUpdatedAt());
    }
}
