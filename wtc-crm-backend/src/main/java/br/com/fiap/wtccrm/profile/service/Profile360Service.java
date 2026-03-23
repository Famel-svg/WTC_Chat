package br.com.fiap.wtccrm.profile.service;

import br.com.fiap.wtccrm.campaigns.dto.CampaignDTO;
import br.com.fiap.wtccrm.campaigns.service.CampaignService;
import br.com.fiap.wtccrm.chat.dto.MessageDTO;
import br.com.fiap.wtccrm.chat.service.ChatService;
import br.com.fiap.wtccrm.clients.dto.ClientDTO;
import br.com.fiap.wtccrm.clients.service.ClientService;
import br.com.fiap.wtccrm.profile.dto.Profile360DTO;
import br.com.fiap.wtccrm.tasks.dto.TaskDTO;
import br.com.fiap.wtccrm.tasks.service.TaskService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class Profile360Service {

    private final ClientService clientService;
    private final ChatService chatService;
    private final CampaignService campaignService;
    private final TaskService taskService;

    public Profile360Service(
            ClientService clientService,
            ChatService chatService,
            CampaignService campaignService,
            TaskService taskService) {
        this.clientService = clientService;
        this.chatService = chatService;
        this.campaignService = campaignService;
        this.taskService = taskService;
    }

    public Profile360DTO getProfile(String clientId) {
        ClientDTO client = clientService.findById(clientId);
        List<MessageDTO> messages = chatService.listConversations().stream()
                .filter(c -> c.participantIds() != null && c.participantIds().contains(clientId))
                .flatMap(c -> chatService.listMessages(c.id()).stream())
                .sorted((a, b) -> b.sentAt().compareTo(a.sentAt()))
                .limit(5)
                .toList();
        List<CampaignDTO> campaigns = campaignService.findAll().stream()
                .filter(c -> client.segmentId() != null && client.segmentId().equals(c.targetSegmentId()))
                .limit(3)
                .toList();
        List<TaskDTO> openTasks = taskService.list(clientId, "OPEN", null);
        return new Profile360DTO(client, messages, campaigns, openTasks);
    }
}
