package com.bupt.starmap.controller;

import com.bupt.starmap.domain.Stargazer;
import com.bupt.starmap.service.StargazerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StargazerController {
    private final StargazerService stargazerService;

    @GetMapping("/stargazers")
    public ResponseEntity<List<Stargazer>> getStargazers() {
        return ResponseEntity.ok().body(stargazerService.getStargazers());
    }
}
