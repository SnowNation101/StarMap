/*
 * Copyright (C) 2022 David "SnowNation" Zhang
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License
 *  is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *  or implied. See the License for the specific language governing permissions and limitations under
 *  the License.
 */

package com.bupt.starmap.service;

import com.bupt.starmap.domain.Node;
import com.bupt.starmap.repo.NodeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * The node service implementation
 * @author David Zhang
 * @version 1.0
 */
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
  public List<Node> saveNodes(List<Node> nodes, String username) {
    for (Node node : nodes) {
      node.setUsername(username);
    }
    nodeRepo.deleteAllByUsername(username);
    return nodeRepo.saveAll(nodes);
  }

}
