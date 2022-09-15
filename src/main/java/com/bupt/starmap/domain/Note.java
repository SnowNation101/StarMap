package com.bupt.starmap.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {
  @Id
  @GeneratedValue(strategy = AUTO)
  private Long noteId;
  private String username;
  private String title;
  private LocalDateTime createTime;
  private String content;
  private String category;
}
