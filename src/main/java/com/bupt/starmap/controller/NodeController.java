package com.bupt.starmap.controller;

import com.bupt.starmap.domain.Node;
import com.bupt.starmap.service.NodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/node")
@RequiredArgsConstructor
public class NodeController {

  private final NodeService nodeService;

  @GetMapping("/get/all")
  public List<Node> getNodes() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return nodeService.getNodes(username);
  }

  @PostMapping("/save/all")
  public ResponseEntity<List<Node>> saveNodes(@RequestBody List<Node> nodes) {
    URI uri = URI.create(ServletUriComponentsBuilder
        .fromCurrentContextPath().path("/save/all").toUriString());
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.created(uri).body(nodeService.saveNodes(nodes, username));
  }

}
