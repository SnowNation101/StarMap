package com.bupt.starmap.controller;

import com.bupt.starmap.domain.Node;
import com.bupt.starmap.service.NodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/node")
@RequiredArgsConstructor
public class NodeController {

    private final NodeService nodeService;

    @GetMapping("/get_nodes/{username}")
    public List<Node> getNodes(@PathVariable String username) {
        return nodeService.getNodes(username);
    }

    
}
