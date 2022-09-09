package com.bupt.starmap.repo;

import com.bupt.starmap.domain.WebCrawler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebCrawlerRepo extends JpaRepository<WebCrawler, Long> {
}
