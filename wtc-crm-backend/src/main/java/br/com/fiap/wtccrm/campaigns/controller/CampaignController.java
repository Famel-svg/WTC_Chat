package br.com.fiap.wtccrm.campaigns.controller;

import br.com.fiap.wtccrm.campaigns.dto.CampaignDTO;
import br.com.fiap.wtccrm.campaigns.service.CampaignService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignService service;

    public CampaignController(CampaignService service) {
        this.service = service;
    }

    @GetMapping
    public List<CampaignDTO> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public CampaignDTO byId(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CampaignDTO create(@RequestBody CampaignDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public CampaignDTO update(@PathVariable String id, @RequestBody CampaignDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @PostMapping("/{id}/send")
    public CampaignDTO send(@PathVariable String id) {
        return service.sendCampaign(id);
    }
}
