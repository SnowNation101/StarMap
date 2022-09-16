/*
 * Copyright (C) 2022 David "SnowNation" Zhang
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License
 *  is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *  or implied. See the License for the specific language governing permissions and limitations under
 *  the License.
 */

package com.bupt.starmap.config;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Locale configuration, switching languages
 * @author David Zhang
 * @version 1.0
 */
public class LocaleConfig implements LocaleResolver {

  @Override
  public Locale resolveLocale(HttpServletRequest request) {
    String language = request.getParameter("l");

    Locale locale = Locale.getDefault();

    if (!StringUtils.isEmpty(language)) {
      // zh_CN
      String[] split = language.split("_");
      locale = new Locale(split[0], split[1]);
    }
    return locale;
  }

  @Override
  public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

  }
}
