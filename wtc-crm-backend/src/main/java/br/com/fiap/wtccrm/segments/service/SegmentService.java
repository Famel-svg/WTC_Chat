package br.com.fiap.wtccrm.segments.service;

import br.com.fiap.wtccrm.clients.dto.ClientDTO;
import br.com.fiap.wtccrm.clients.service.ClientService;
import br.com.fiap.wtccrm.exception.BusinessException;
import br.com.fiap.wtccrm.persistence.document.SegmentDocument;
import br.com.fiap.wtccrm.persistence.repository.ClientRepository;
import br.com.fiap.wtccrm.persistence.repository.SegmentRepository;
import br.com.fiap.wtccrm.segments.dto.SegmentDTO;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class SegmentService {

    private final SegmentRepository segmentRepository;
    private final ClientRepository clientRepository;
    private final ClientService clientService;

    public SegmentService(SegmentRepository segmentRepository, ClientRepository clientRepository, ClientService clientService) {
        this.segmentRepository = segmentRepository;
        this.clientRepository = clientRepository;
        this.clientService = clientService;
    }

    public List<SegmentDTO> findAll() {
        return segmentRepository.findAll().stream().map(this::toDtoWithCount).toList();
    }

    public SegmentDTO findById(String id) {
        SegmentDocument doc = segmentRepository.findById(id).orElseThrow(() -> new BusinessException("Segment not found"));
        return toDtoWithCount(doc);
    }

    public SegmentDTO create(SegmentDTO request) {
        SegmentDocument doc = new SegmentDocument();
        doc.setId(UUID.randomUUID().toString());
        doc.setName(request.name());
        doc.setDescription(request.description());
        doc.setColor(request.color() == null ? "#2F80ED" : request.color());
        doc.setCreatedAt(Instant.now());
        segmentRepository.save(doc);
        return toDtoWithCount(doc);
    }

    public SegmentDTO update(String id, SegmentDTO request) {
        SegmentDocument current = segmentRepository.findById(id).orElseThrow(() -> new BusinessException("Segment not found"));
        current.setName(request.name() == null ? current.getName() : request.name());
        current.setDescription(request.description() == null ? current.getDescription() : request.description());
        current.setColor(request.color() == null ? current.getColor() : request.color());
        segmentRepository.save(current);
        return toDtoWithCount(current);
    }

    public void delete(String id) {
        findById(id);
        segmentRepository.deleteById(id);
    }

    public List<ClientDTO> listClients(String segmentId) {
        findById(segmentId);
        return clientService.findBySegment(segmentId);
    }

    private SegmentDTO toDtoWithCount(SegmentDocument doc) {
        int count = (int) clientRepository.countBySegmentId(doc.getId());
        return new SegmentDTO(
                doc.getId(),
                doc.getName(),
                doc.getDescription(),
                doc.getColor(),
                count,
                doc.getCreatedAt());
    }
}
