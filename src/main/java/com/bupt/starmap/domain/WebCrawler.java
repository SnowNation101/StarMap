package com.bupt.starmap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebCrawler {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long crawlerId;
    private String crawlerTitle;
    @Column(columnDefinition = "varchar(511)")
    private String url;
    private String views;
    private String likes;
    private String comments;


}
