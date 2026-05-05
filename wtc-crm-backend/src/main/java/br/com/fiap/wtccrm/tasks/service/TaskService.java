package br.com.fiap.wtccrm.tasks.service;

import br.com.fiap.wtccrm.exception.BusinessException;
import br.com.fiap.wtccrm.persistence.document.TaskDocument;
import br.com.fiap.wtccrm.persistence.repository.TaskRepository;
import br.com.fiap.wtccrm.tasks.dto.TaskDTO;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDTO> list(String clientId, String status, String operatorId) {
        return taskRepository.findAll().stream()
                .filter(t -> clientId == null || clientId.equals(t.getClientId()))
                .filter(t -> status == null || status.equalsIgnoreCase(t.getStatus()))
                .filter(t -> operatorId == null || operatorId.equals(t.getOperatorId()))
                .map(this::toDto)
                .toList();
    }

    public TaskDTO findById(String id) {
        return taskRepository.findById(id).map(this::toDto).orElseThrow(() -> new BusinessException("Task not found"));
    }

    public TaskDTO create(TaskDTO request) {
        Instant now = Instant.now();
        TaskDocument doc = new TaskDocument();
        doc.setId(UUID.randomUUID().toString());
        doc.setTitle(request.title());
        doc.setDescription(request.description());
        doc.setClientId(request.clientId());
        doc.setOperatorId(request.operatorId());
        doc.setStatus(request.status() == null ? "OPEN" : request.status());
        doc.setPriority(request.priority() <= 0 ? 1 : request.priority());
        doc.setDueDate(request.dueDate());
        doc.setCreatedAt(now);
        doc.setUpdatedAt(now);
        taskRepository.save(doc);
        return toDto(doc);
    }

    public TaskDTO update(String id, TaskDTO request) {
        TaskDocument current = taskRepository.findById(id).orElseThrow(() -> new BusinessException("Task not found"));
        current.setTitle(request.title() == null ? current.getTitle() : request.title());
        current.setDescription(request.description() == null ? current.getDescription() : request.description());
        current.setClientId(request.clientId() == null ? current.getClientId() : request.clientId());
        current.setOperatorId(request.operatorId() == null ? current.getOperatorId() : request.operatorId());
        current.setStatus(request.status() == null ? current.getStatus() : request.status());
        current.setPriority(request.priority() <= 0 ? current.getPriority() : request.priority());
        current.setDueDate(request.dueDate() == null ? current.getDueDate() : request.dueDate());
        current.setUpdatedAt(Instant.now());
        taskRepository.save(current);
        return toDto(current);
    }

    public void delete(String id) {
        findById(id);
        taskRepository.deleteById(id);
    }

    private TaskDTO toDto(TaskDocument t) {
        return new TaskDTO(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.getClientId(),
                t.getOperatorId(),
                t.getStatus(),
                t.getPriority(),
                t.getDueDate(),
                t.getCreatedAt(),
                t.getUpdatedAt());
    }
}
