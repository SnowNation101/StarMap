package com.bupt.starmap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.util.Date;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StarMap {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long mapId;
    private String username;
    private String current;
    private Date editTime;
}
