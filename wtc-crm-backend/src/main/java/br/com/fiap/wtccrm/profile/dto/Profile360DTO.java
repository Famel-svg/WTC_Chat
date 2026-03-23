package br.com.fiap.wtccrm.profile.dto;

import br.com.fiap.wtccrm.campaigns.dto.CampaignDTO;
import br.com.fiap.wtccrm.chat.dto.MessageDTO;
import br.com.fiap.wtccrm.clients.dto.ClientDTO;
import br.com.fiap.wtccrm.tasks.dto.TaskDTO;
import java.util.List;

public record Profile360DTO(
        ClientDTO client,
        List<MessageDTO> recentMessages,
        List<CampaignDTO> recentCampaigns,
        List<TaskDTO> openTasks) {
}
