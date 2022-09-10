package com.bupt.starmap.controller;

import com.bupt.starmap.domain.Node;
import com.bupt.starmap.service.NodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/node")
@RequiredArgsConstructor
public class NodeController {

    private final NodeService nodeService;

    @GetMapping("/get/{username}")
    public List<Node> getNodes(@PathVariable String username) {
        return nodeService.getNodes(username);
    }

    @PostMapping("/post")
    public ResponseEntity<List<Node>> saveNodes(@RequestBody List<Node> nodes) {
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/node/post/{username}").toUriString());
        return ResponseEntity.created(uri).body(nodeService.saveNodes(nodes));
    }

}
