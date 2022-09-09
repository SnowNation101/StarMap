package com.bupt.starmap.repo;

import com.bupt.starmap.domain.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeRepo extends JpaRepository<Node, Long> {
    List<Node> findAllByUsername(String username);
}
