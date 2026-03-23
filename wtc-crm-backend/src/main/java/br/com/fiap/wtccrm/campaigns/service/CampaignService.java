package br.com.fiap.wtccrm.campaigns.service;

import br.com.fiap.wtccrm.campaigns.dto.CampaignDTO;
import br.com.fiap.wtccrm.clients.service.ClientService;
import br.com.fiap.wtccrm.exception.BusinessException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class CampaignService {

    private final Map<String, CampaignDTO> campaigns = new ConcurrentHashMap<>();
    private final ClientService clientService;

    public CampaignService(ClientService clientService) {
        this.clientService = clientService;
    }

    public List<CampaignDTO> findAll() {
        return campaigns.values().stream().toList();
    }

    public CampaignDTO findById(String id) {
        CampaignDTO dto = campaigns.get(id);
        if (dto == null) {
            throw new BusinessException("Campaign not found");
        }
        return dto;
    }

    public CampaignDTO create(CampaignDTO request) {
        CampaignDTO dto = new CampaignDTO(
                UUID.randomUUID().toString(),
                request.title(),
                request.description(),
                request.deeplink(),
                request.mediaUrl(),
                request.targetSegmentId(),
                request.status() == null ? "DRAFT" : request.status(),
                request.scheduledAt(),
                null,
                Instant.now(),
                0);
        campaigns.put(dto.id(), dto);
        return dto;
    }

    public CampaignDTO update(String id, CampaignDTO request) {
        CampaignDTO current = findById(id);
        CampaignDTO updated = new CampaignDTO(
                current.id(),
                request.title() == null ? current.title() : request.title(),
                request.description() == null ? current.description() : request.description(),
                request.deeplink() == null ? current.deeplink() : request.deeplink(),
                request.mediaUrl() == null ? current.mediaUrl() : request.mediaUrl(),
                request.targetSegmentId() == null ? current.targetSegmentId() : request.targetSegmentId(),
                request.status() == null ? current.status() : request.status(),
                request.scheduledAt() == null ? current.scheduledAt() : request.scheduledAt(),
                current.sentAt(),
                current.createdAt(),
                current.recipientCount());
        campaigns.put(id, updated);
        return updated;
    }

    public void delete(String id) {
        findById(id);
        campaigns.remove(id);
    }

    public CampaignDTO sendCampaign(String id) {
        CampaignDTO current = findById(id);
        int recipients = clientService.findBySegment(current.targetSegmentId()).size();
        CampaignDTO sent = new CampaignDTO(
                current.id(),
                current.title(),
                current.description(),
                current.deeplink(),
                current.mediaUrl(),
                current.targetSegmentId(),
                "SENT",
                current.scheduledAt(),
                Instant.now(),
                current.createdAt(),
                recipients);
        campaigns.put(id, sent);
        return sent;
    }
}
