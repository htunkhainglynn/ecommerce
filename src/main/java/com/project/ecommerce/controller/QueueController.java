package com.project.ecommerce.controller;

import com.project.ecommerce.service.QueueInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/queue")
public class QueueController {

    private final QueueInfoService queueInfoService;

    @Autowired
    public QueueController(QueueInfoService queueInfoService) {
        this.queueInfoService = queueInfoService;
    }

    @GetMapping
    public ResponseEntity<String> getQueueInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(queueInfoService.getQueueNameByUsername(username));
    }
}
