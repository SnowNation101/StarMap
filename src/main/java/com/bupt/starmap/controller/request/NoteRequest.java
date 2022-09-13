package com.bupt.starmap.controller.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class NoteRequest implements Serializable {

  private static final long serialVersionUID = 1L;
  private String title;
  private String content;
  private String category;
}
