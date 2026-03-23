package br.com.fiap.wtccrm.tasks.service;

import br.com.fiap.wtccrm.exception.BusinessException;
import br.com.fiap.wtccrm.tasks.dto.TaskDTO;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final Map<String, TaskDTO> tasks = new ConcurrentHashMap<>();

    public List<TaskDTO> list(String clientId, String status, String operatorId) {
        return tasks.values().stream()
                .filter(t -> clientId == null || clientId.equals(t.clientId()))
                .filter(t -> status == null || status.equalsIgnoreCase(t.status()))
                .filter(t -> operatorId == null || operatorId.equals(t.operatorId()))
                .toList();
    }

    public TaskDTO findById(String id) {
        TaskDTO dto = tasks.get(id);
        if (dto == null) {
            throw new BusinessException("Task not found");
        }
        return dto;
    }

    public TaskDTO create(TaskDTO request) {
        Instant now = Instant.now();
        TaskDTO dto = new TaskDTO(
                UUID.randomUUID().toString(),
                request.title(),
                request.description(),
                request.clientId(),
                request.operatorId(),
                request.status() == null ? "OPEN" : request.status(),
                request.priority() <= 0 ? 1 : request.priority(),
                request.dueDate(),
                now,
                now);
        tasks.put(dto.id(), dto);
        return dto;
    }

    public TaskDTO update(String id, TaskDTO request) {
        TaskDTO current = findById(id);
        TaskDTO updated = new TaskDTO(
                current.id(),
                request.title() == null ? current.title() : request.title(),
                request.description() == null ? current.description() : request.description(),
                request.clientId() == null ? current.clientId() : request.clientId(),
                request.operatorId() == null ? current.operatorId() : request.operatorId(),
                request.status() == null ? current.status() : request.status(),
                request.priority() <= 0 ? current.priority() : request.priority(),
                request.dueDate() == null ? current.dueDate() : request.dueDate(),
                current.createdAt(),
                Instant.now());
        tasks.put(id, updated);
        return updated;
    }

    public void delete(String id) {
        findById(id);
        tasks.remove(id);
    }
}
