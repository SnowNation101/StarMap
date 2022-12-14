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

package com.bupt.starmap.controller;

import com.bupt.starmap.domain.WebCrawler;
import com.bupt.starmap.repo.WebCrawlerRepo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The main web crawler, crawling specific contents from {@code CSDN}
 * @author Aijia Lei, David Zhang
 * @version 1.0
 */
@Slf4j
@RequestMapping("/api")
@RestController
public class WebCrawlerController {
  @Autowired
  WebCrawlerRepo webCrawlerRepo;

  /**
   * Crawl from {@code CSDN}
   * @param q The question
   * @return List of crawled results
   */
  @PostMapping("/crawl")
  public List<WebCrawler> crawl(@RequestBody String q) {

    System.getProperties().setProperty("webdriver.chrome.driver", "./src/main/resources/chromedriver.exe");
    System.out.println("准备实例化ChromeOpen类");

    // 防反爬
    ChromeOptions options = new ChromeOptions();
    // 关闭界面上的---Chrome正在受到自动软件的控制
    options.addArguments("disable-infobars");

    // 设置ExperimentalOption
    List<String> excludeSwitches = Lists.newArrayList("enable-automation");
    options.setExperimentalOption("excludeSwitches", excludeSwitches);
    options.setExperimentalOption("useAutomationExtension", false);
    ChromeDriver chromeDriver = new ChromeDriver(options);

    // 修改window.navigator.webdirver=undefined，防机器人识别机制
    HashMap<String, Object> command = new HashMap<>();
    command.put("source", "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
    chromeDriver.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", command);

    String url = "https://www.csdn.net/";
    chromeDriver.get(url);

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    WebElement inputrect = chromeDriver.findElement(By.id("toolbar-search-input")); // 找到输入的方框
    inputrect.sendKeys(q);

    WebElement searchbutton = chromeDriver.findElement(By.id("toolbar-search-button")); // 点击搜索按钮
    searchbutton.click();

    chromeDriver.switchTo().window((String) chromeDriver.getWindowHandles().toArray()[1]); // 切换到新页面
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    WebElement blogbutton = chromeDriver.findElement(By.xpath("/html/body/div[2]/div[2]/div[2]/div[1]/div[1]/div[1]/div/ul/li[2]"));
    System.out.println(blogbutton.getText());
    blogbutton.click(); // 点击博客按钮

    chromeDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS); // 隐式等待页面加载完毕
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    // 点击热门按钮
    WebElement hot = chromeDriver.findElement(By.className("sort")).findElement(By.className("hot"));
    hot.click();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    WebElement content = chromeDriver.findElement(By.xpath("/html/body/div[2]/div[2]/div[2]/div[1]/div[2]/div"));
    List<WebElement> articleCards = content.findElements(By.cssSelector(".so-items-normal"));

    //删除原表的数据
    webCrawlerRepo.deleteAll();
    for (WebElement article : articleCards) {
      WebElement a = article.findElement(By.cssSelector("h3 a"));
      String text = a.getText();
      String href = a.getAttribute("href");

      WebElement datablock = article.findElement(By.className("btm-lt"));
      String view = datablock.findElement(By.className("btm-view")).findElement(By.className("num")).getText();

      String like = "";
      By by = new By.ByClassName("btm-dig");
      if (this.isExist(datablock, by)) {
        like = datablock.findElement(By.className("btm-dig")).findElement(By.className("num")).getText();
      } else {
        like = "0";
      }

      String comment = "";
      by = new By.ByClassName("btm-comment");
      if (this.isExist(datablock, by)) {
        comment = datablock.findElement(By.className("btm-comment")).findElement(By.className("num")).getText();
      } else {
        comment = "0";
      }
      System.out.println("view:" + view + " like: " + like + " comment: " + comment);

      //插入数据
      webCrawlerRepo.save(new WebCrawler(null, text, href, view, like, comment));
      log.info("Saved url {}", webCrawlerRepo.count());

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      if (webCrawlerRepo.count() == 8) break;
    }

//    try {
//      Thread.sleep(3000);
//    } catch (InterruptedException e) {
//      throw new RuntimeException(e);
//    }
    log.info("Web crawling finished");
    chromeDriver.quit();
    return webCrawlerRepo.findAll();
  }


  //判断是否存在某元素
  boolean isExist(WebElement remoteWebDriver, By by) {
    try {
      remoteWebDriver.findElement(by);
      return true;
    } catch (Exception e) {
      log.error("Element not found");
      return false;
    }
  }

}

