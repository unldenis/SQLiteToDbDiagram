package com.github.unldenis.sqlitetodbdiagram.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class Selenium {

  private final App app;

  public Selenium(App app) {
    this.app = app;
  }

  public void start(List<String> lines) throws InterruptedException {
    WebDriverManager.chromedriver().setup();

//    ChromeOptions options = new ChromeOptions();
//    options.addArguments("--headless", "--window-size=1920,1200","--ignore-certificate-errors");

    ChromeDriver driver = new ChromeDriver();
    Actions builder = new Actions(driver);

    app.addProgress(10);

    //Maximize the browser
    driver.manage().window().setSize(new Dimension(1280, 720));

    driver.manage().window().setPosition(new Point(700, 300));

    //Launch website
    driver.navigate().to("https://dbdiagram.io/d");

    //Hover Import
    WebElement importElement = driver.findElement(By.xpath("(//span[normalize-space()='Import'])[1]"));
    builder.moveToElement(importElement).perform();

    app.addProgress(10);

    // Click SQL Server
    WebElement sqlElement = driver.findElement(
        By.xpath("(//div[contains(text(),'Import from SQL Server')])[1]"));
    sqlElement.click();

    app.addProgress(10);

    Robot rb;
    try {
      rb = new Robot();
    } catch (AWTException e) {
      throw new RuntimeException(e);
    }

    // Move Mouse to TextArea
    rb.mouseMove(1000, 700);

    sleep();

    // Click Mouse to TextArea
    click(rb);

    // Paste lines to TextArea
    paste(rb, String.join("", lines));

    app.addProgress(10);

    sleep();

    // Click to Submit
    WebElement submitElements = driver.findElement(By.xpath("(//div[@id='import-submit-btn'])[1]"));
    submitElements.click();

    sleep();

    // Move to the left
    rb.mouseMove(800, 500);

    app.addProgress(10);

    sleep();

    // Click to the Text Area
    click(rb);

    sleep();

    // Select All
    control_key(rb, KeyEvent.VK_A);

    sleep();

    // Copy
    control_key(rb, KeyEvent.VK_C);

    app.addProgress(10);

    driver.manage().window().maximize();

  }

  private void sleep() throws InterruptedException {
    Thread.sleep(300);
  }

  private void paste(Robot rb, String data) throws InterruptedException {
    StringSelection textToPaste = new StringSelection(data);

    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(textToPaste, null);

    control_key(rb, KeyEvent.VK_V);
  }

  private void control_key(Robot rb, int keyCode) throws InterruptedException {
    rb.keyPress(KeyEvent.VK_CONTROL);
    sleep();
    rb.keyPress(keyCode);
    sleep();
    rb.keyRelease(keyCode);
    sleep();
    rb.keyRelease(KeyEvent.VK_CONTROL);
  }

  private void click(Robot rb) throws InterruptedException {
    rb.mousePress(InputEvent.BUTTON1_MASK);
    sleep();
    rb.mouseRelease(InputEvent.BUTTON1_MASK);
  }

}
