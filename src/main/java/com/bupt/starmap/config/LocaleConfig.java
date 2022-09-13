package com.bupt.starmap.config;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class LocaleConfig implements LocaleResolver {

  @Override
  public Locale resolveLocale(HttpServletRequest request) {
    String language = request.getParameter("l");

    Locale locale = Locale.getDefault();

    if (!StringUtils.isEmpty(language)) {
      // zh_CN
      String[] split = language.split("_");
      //
      locale = new Locale(split[0], split[1]);
    }
    return locale;
  }

  @Override
  public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

  }
}
