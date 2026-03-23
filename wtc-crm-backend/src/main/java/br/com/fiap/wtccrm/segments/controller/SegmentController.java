package br.com.fiap.wtccrm.segments.controller;

import br.com.fiap.wtccrm.clients.dto.ClientDTO;
import br.com.fiap.wtccrm.segments.dto.SegmentDTO;
import br.com.fiap.wtccrm.segments.service.SegmentService;
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
@RequestMapping("/api/segments")
public class SegmentController {

    private final SegmentService service;

    public SegmentController(SegmentService service) {
        this.service = service;
    }

    @GetMapping
    public List<SegmentDTO> listAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public SegmentDTO findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SegmentDTO create(@RequestBody SegmentDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public SegmentDTO update(@PathVariable String id, @RequestBody SegmentDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @GetMapping("/{id}/clients")
    public List<ClientDTO> listClients(@PathVariable String id) {
        return service.listClients(id);
    }
}
