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

/**
 * The Node Controller
 * @author David Zhang
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/node")
@RequiredArgsConstructor
public class NodeController {

  private final NodeService nodeService;

  /**
   * Get all nodes of current login user for starmap
   * @return List of nodes
   */
  @GetMapping("/get/all")
  public List<Node> getNodes() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return nodeService.getNodes(username);
  }

  /**
   * Save all nodes of current login user to database
   * @param nodes
   * @return List of save nodes
   */
  @PostMapping("/save/all")
  public ResponseEntity<List<Node>> saveNodes(@RequestBody List<Node> nodes) {
    URI uri = URI.create(ServletUriComponentsBuilder
        .fromCurrentContextPath().path("/save/all").toUriString());
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.created(uri).body(nodeService.saveNodes(nodes, username));
  }

}
