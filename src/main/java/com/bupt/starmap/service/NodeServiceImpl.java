package com.bupt.starmap.service;

import com.bupt.starmap.domain.Node;
import com.bupt.starmap.repo.NodeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NodeServiceImpl implements NodeService {

    private final NodeRepo nodeRepo;

    @Override
    public List<Node> getNodes(String username) {
        return nodeRepo.findAllByUsername(username);
    }

    @Override
    public List<Node> saveNodes(List<Node> nodes) {
        nodeRepo.deleteAll();
        return nodeRepo.saveAll(nodes);
    }

    @Override
    public void deleteNode(Long nodeId) {
        nodeRepo.deleteByNodeId(nodeId);
    }

}
