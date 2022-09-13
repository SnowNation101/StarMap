package com.bupt.starmap.service;

import com.bupt.starmap.domain.Node;

import java.util.List;

public interface NodeService {
    List<Node> getNodes(String username);

    List<Node> saveNodes(List<Node> nodes);

    void deleteNode(Long nodeId);
}
