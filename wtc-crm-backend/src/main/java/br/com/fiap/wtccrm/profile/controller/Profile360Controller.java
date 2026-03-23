package br.com.fiap.wtccrm.profile.controller;

import br.com.fiap.wtccrm.profile.dto.Profile360DTO;
import br.com.fiap.wtccrm.profile.service.Profile360Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile360")
public class Profile360Controller {

    private final Profile360Service service;

    public Profile360Controller(Profile360Service service) {
        this.service = service;
    }

    @GetMapping("/{clientId}")
    public Profile360DTO profile(@PathVariable String clientId) {
        return service.getProfile(clientId);
    }
}
