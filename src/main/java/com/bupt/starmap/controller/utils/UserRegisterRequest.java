package com.bupt.starmap.controller.utils;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private String username;
  private String nickname;
  private String password;

}
