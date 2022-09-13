package com.bupt.starmap.controller;

import com.bupt.starmap.domain.Node;
import com.bupt.starmap.service.NodeService;
import com.bupt.starmap.service.UserService;
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
@CrossOrigin("*")
@RequestMapping("/node")
@RequiredArgsConstructor
public class NodeController {

    private final NodeService nodeService;
    private final UserService userService;

    @GetMapping("/get")
    public List<Node> getNodes() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return nodeService.getNodes(username);
    }

    @PostMapping("/save")
    public ResponseEntity<List<Node>> saveNodes(@RequestBody List<Node> nodes) {
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/node/post/{username}").toUriString());
        return ResponseEntity.created(uri).body(nodeService.saveNodes(nodes));
    }

}
