package br.com.fiap.wtccrm.campaigns.service;

import br.com.fiap.wtccrm.campaigns.dto.CampaignDTO;
import br.com.fiap.wtccrm.clients.dto.ClientDTO;
import br.com.fiap.wtccrm.clients.service.ClientService;
import br.com.fiap.wtccrm.exception.BusinessException;
import br.com.fiap.wtccrm.notifications.service.PushNotificationService;
import br.com.fiap.wtccrm.persistence.document.CampaignDocument;
import br.com.fiap.wtccrm.persistence.repository.CampaignRepository;
import br.com.fiap.wtccrm.persistence.repository.UserRepository;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final ClientService clientService;
    private final PushNotificationService pushNotificationService;
    private final UserRepository userRepository;

    public CampaignService(
            CampaignRepository campaignRepository,
            ClientService clientService,
            PushNotificationService pushNotificationService,
            UserRepository userRepository) {
        this.campaignRepository = campaignRepository;
        this.clientService = clientService;
        this.pushNotificationService = pushNotificationService;
        this.userRepository = userRepository;
    }

    public List<CampaignDTO> findAll() {
        return campaignRepository.findAll().stream().map(this::toDto).toList();
    }

    public CampaignDTO findById(String id) {
        return campaignRepository.findById(id).map(this::toDto).orElseThrow(() -> new BusinessException("Campaign not found"));
    }

    public CampaignDTO create(CampaignDTO request) {
        CampaignDocument doc = new CampaignDocument();
        doc.setId(UUID.randomUUID().toString());
        doc.setTitle(request.title());
        doc.setDescription(request.description());
        doc.setDeeplink(request.deeplink());
        doc.setMediaUrl(request.mediaUrl());
        doc.setTargetSegmentId(request.targetSegmentId());
        doc.setStatus(request.status() == null ? "DRAFT" : request.status());
        doc.setScheduledAt(request.scheduledAt());
        doc.setSentAt(null);
        doc.setCreatedAt(Instant.now());
        doc.setRecipientCount(0);
        campaignRepository.save(doc);
        return toDto(doc);
    }

    public CampaignDTO update(String id, CampaignDTO request) {
        CampaignDocument current = campaignRepository.findById(id).orElseThrow(() -> new BusinessException("Campaign not found"));
        current.setTitle(request.title() == null ? current.getTitle() : request.title());
        current.setDescription(request.description() == null ? current.getDescription() : request.description());
        current.setDeeplink(request.deeplink() == null ? current.getDeeplink() : request.deeplink());
        current.setMediaUrl(request.mediaUrl() == null ? current.getMediaUrl() : request.mediaUrl());
        current.setTargetSegmentId(request.targetSegmentId() == null ? current.getTargetSegmentId() : request.targetSegmentId());
        current.setStatus(request.status() == null ? current.getStatus() : request.status());
        current.setScheduledAt(request.scheduledAt() == null ? current.getScheduledAt() : request.scheduledAt());
        campaignRepository.save(current);
        return toDto(current);
    }

    public void delete(String id) {
        findById(id);
        campaignRepository.deleteById(id);
    }

    public CampaignDTO sendCampaign(String id) {
        CampaignDocument current = campaignRepository.findById(id).orElseThrow(() -> new BusinessException("Campaign not found"));
        int recipients = clientService.findBySegment(current.getTargetSegmentId()).size();
        current.setStatus("SENT");
        current.setSentAt(Instant.now());
        current.setRecipientCount(recipients);
        campaignRepository.save(current);
        CampaignDTO dto = toDto(current);
        notifyCampaignPush(dto);
        return dto;
    }

    /** Dispara FCM para usuários cujo e-mail coincide com clientes do segmento alvo. */
    private void notifyCampaignPush(CampaignDTO campaign) {
        List<ClientDTO> clients = clientService.findBySegment(campaign.targetSegmentId());
        for (ClientDTO c : clients) {
            if (c.email() == null || c.email().isBlank()) {
                continue;
            }
            userRepository
                    .findByEmailIgnoreCase(c.email().toLowerCase(Locale.ROOT))
                    .ifPresent(user -> {
                        if (user.getFcmToken() == null || user.getFcmToken().isBlank()) {
                            return;
                        }
                        Map<String, String> data = new HashMap<>();
                        data.put("type", "CAMPAIGN");
                        data.put("campaignId", campaign.id());
                        if (campaign.deeplink() != null) {
                            data.put("deeplink", campaign.deeplink());
                        }
                        String body = campaign.description() != null && !campaign.description().isBlank()
                                ? campaign.description()
                                : campaign.title();
                        pushNotificationService.sendPush(user.getFcmToken(), campaign.title(), body, data);
                    });
        }
    }

    private CampaignDTO toDto(CampaignDocument c) {
        return new CampaignDTO(
                c.getId(),
                c.getTitle(),
                c.getDescription(),
                c.getDeeplink(),
                c.getMediaUrl(),
                c.getTargetSegmentId(),
                c.getStatus(),
                c.getScheduledAt(),
                c.getSentAt(),
                c.getCreatedAt(),
                c.getRecipientCount());
    }
}
