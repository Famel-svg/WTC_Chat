package br.com.fiap.wtccrm.clients.controller;

import br.com.fiap.wtccrm.clients.dto.ClientDTO;
import br.com.fiap.wtccrm.clients.service.ClientService;
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
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @GetMapping
    public List<ClientDTO> listAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ClientDTO findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDTO create(@RequestBody ClientDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public ClientDTO update(@PathVariable String id, @RequestBody ClientDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @GetMapping("/segment/{segmentId}")
    public List<ClientDTO> bySegment(@PathVariable String segmentId) {
        return service.findBySegment(segmentId);
    }
}
