package com.bupt.starmap.controller.utils;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UserUpdateRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private String nickname;
  private String password;
  private LocalDate dob;
  private String phoneNo;

}
