package br.com.fiap.wtccrm.segments.service;

import br.com.fiap.wtccrm.clients.dto.ClientDTO;
import br.com.fiap.wtccrm.clients.service.ClientService;
import br.com.fiap.wtccrm.exception.BusinessException;
import br.com.fiap.wtccrm.segments.dto.SegmentDTO;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class SegmentService {

    private final Map<String, SegmentDTO> segments = new ConcurrentHashMap<>();
    private final ClientService clientService;

    public SegmentService(ClientService clientService) {
        this.clientService = clientService;
    }

    public List<SegmentDTO> findAll() {
        return segments.values().stream().toList();
    }

    public SegmentDTO findById(String id) {
        SegmentDTO dto = segments.get(id);
        if (dto == null) {
            throw new BusinessException("Segment not found");
        }
        return dto;
    }

    public SegmentDTO create(SegmentDTO request) {
        SegmentDTO dto = new SegmentDTO(
                UUID.randomUUID().toString(),
                request.name(),
                request.description(),
                request.color() == null ? "#2F80ED" : request.color(),
                0,
                Instant.now());
        segments.put(dto.id(), dto);
        return dto;
    }

    public SegmentDTO update(String id, SegmentDTO request) {
        SegmentDTO current = findById(id);
        SegmentDTO updated = new SegmentDTO(
                current.id(),
                request.name() == null ? current.name() : request.name(),
                request.description() == null ? current.description() : request.description(),
                request.color() == null ? current.color() : request.color(),
                current.clientCount(),
                current.createdAt());
        segments.put(id, updated);
        return updated;
    }

    public void delete(String id) {
        findById(id);
        segments.remove(id);
    }

    public List<ClientDTO> listClients(String segmentId) {
        findById(segmentId);
        return clientService.findBySegment(segmentId);
    }
}
