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

package com.bupt.starmap.repo;

import com.bupt.starmap.domain.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Node JPA repository
 * @author David Zhang
 * @version 1.0
 */
@Repository
public interface NodeRepo extends JpaRepository<Node, Long> {
  List<Node> findAllByUsername(String username);
  void deleteAllByUsername(String username);
}
