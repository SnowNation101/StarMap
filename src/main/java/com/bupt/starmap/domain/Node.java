package com.bupt.starmap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Node {
    @Id
    private Long nodeId;
    private String value;
    private Long parentId;
    private String username;
}
