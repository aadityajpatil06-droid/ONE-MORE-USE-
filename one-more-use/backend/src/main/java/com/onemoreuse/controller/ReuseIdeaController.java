package com.onemoreuse.controller;

import com.onemoreuse.dto.ReuseSuggestionDto;
import com.onemoreuse.service.ReuseIdeaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reuse-ideas")
public class ReuseIdeaController {

    private final ReuseIdeaService reuseIdeaService;

    @Autowired
    public ReuseIdeaController(ReuseIdeaService reuseIdeaService) {
        this.reuseIdeaService = reuseIdeaService;
    }

    /**
     * Analyzes an object by description or keyword and generates 5 practical reuse ideas.
     * Accessible via POST with JSON body: {"objectName": "wooden box", "condition": "Good — minor wear"}
     */
    @PostMapping("/analyze")
    public ResponseEntity<ReuseSuggestionDto> analyzeObject(@RequestBody Map<String, String> request) {
        String input = request.getOrDefault("objectName", request.getOrDefault("description", ""));
        String condition = request.getOrDefault("condition", "Good — minor wear");

        ReuseSuggestionDto result = reuseIdeaService.analyze(input, condition);
        return ResponseEntity.ok(result);
    }
}
