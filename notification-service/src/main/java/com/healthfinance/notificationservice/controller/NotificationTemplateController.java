package com.healthfinance.notificationservice.controller;

import com.healthfinance.notificationservice.model.NotificationTemplate;
import com.healthfinance.notificationservice.repository.NotificationTemplateRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications/templates")
@RequiredArgsConstructor
@Tag(name = "Notification Templates", description = "Endpoints for managing notification templates")
public class NotificationTemplateController {
    
    private final NotificationTemplateRepository templateRepository;
    
    @PostMapping
    @Operation(summary = "Create notification template")
    public ResponseEntity<NotificationTemplate> createTemplate(@RequestBody NotificationTemplate template) {
        NotificationTemplate saved = templateRepository.save(template);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get template by ID")
    public ResponseEntity<NotificationTemplate> getTemplateById(@PathVariable String id) {
        NotificationTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));
        return ResponseEntity.ok(template);
    }
    
    @GetMapping
    @Operation(summary = "Get all templates")
    public ResponseEntity<List<NotificationTemplate>> getAllTemplates() {
        List<NotificationTemplate> templates = templateRepository.findAll();
        return ResponseEntity.ok(templates);
    }
    
    @GetMapping("/name/{templateName}")
    @Operation(summary = "Get template by name")
    public ResponseEntity<NotificationTemplate> getTemplateByName(@PathVariable String templateName) {
        NotificationTemplate template = templateRepository.findByTemplateName(templateName)
                .orElseThrow(() -> new RuntimeException("Template not found"));
        return ResponseEntity.ok(template);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update template")
    public ResponseEntity<NotificationTemplate> updateTemplate(@PathVariable String id, 
                                                               @RequestBody NotificationTemplate template) {
        NotificationTemplate existing = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));
        
        existing.setTemplateName(template.getTemplateName());
        existing.setNotificationType(template.getNotificationType());
        existing.setChannel(template.getChannel());
        existing.setSubject(template.getSubject());
        existing.setBody(template.getBody());
        existing.setHtmlBody(template.getHtmlBody());
        existing.setIsActive(template.getIsActive());
        
        NotificationTemplate updated = templateRepository.save(existing);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete template")
    public ResponseEntity<Void> deleteTemplate(@PathVariable String id) {
        templateRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
