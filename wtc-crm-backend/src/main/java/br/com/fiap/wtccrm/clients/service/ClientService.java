package br.com.fiap.wtccrm.clients.service;

import br.com.fiap.wtccrm.clients.dto.ClientDTO;
import br.com.fiap.wtccrm.exception.BusinessException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final Map<String, ClientDTO> clients = new ConcurrentHashMap<>();

    public List<ClientDTO> findAll() {
        return clients.values().stream().sorted(Comparator.comparing(ClientDTO::createdAt)).toList();
    }

    public ClientDTO findById(String id) {
        ClientDTO dto = clients.get(id);
        if (dto == null) {
            throw new BusinessException("Client not found");
        }
        return dto;
    }

    public ClientDTO create(ClientDTO request) {
        String id = UUID.randomUUID().toString();
        Instant now = Instant.now();
        ClientDTO dto = new ClientDTO(
                id,
                request.name(),
                request.email(),
                request.phone(),
                normalizeCategory(request.category()),
                clampScore(request.score()),
                normalizeStatus(request.status()),
                request.segmentId(),
                request.avatarUrl(),
                now,
                now);
        clients.put(id, dto);
        return dto;
    }

    public ClientDTO update(String id, ClientDTO request) {
        ClientDTO current = findById(id);
        ClientDTO updated = new ClientDTO(
                current.id(),
                request.name() == null ? current.name() : request.name(),
                request.email() == null ? current.email() : request.email(),
                request.phone() == null ? current.phone() : request.phone(),
                request.category() == null ? current.category() : normalizeCategory(request.category()),
                request.score() == 0 ? current.score() : clampScore(request.score()),
                request.status() == null ? current.status() : normalizeStatus(request.status()),
                request.segmentId() == null ? current.segmentId() : request.segmentId(),
                request.avatarUrl() == null ? current.avatarUrl() : request.avatarUrl(),
                current.createdAt(),
                Instant.now());
        clients.put(id, updated);
        return updated;
    }

    public void delete(String id) {
        findById(id);
        clients.remove(id);
    }

    public List<ClientDTO> findBySegment(String segmentId) {
        return clients.values().stream()
                .filter(c -> segmentId.equals(c.segmentId()))
                .toList();
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
}
