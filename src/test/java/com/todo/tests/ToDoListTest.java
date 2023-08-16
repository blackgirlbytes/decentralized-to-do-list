package com.todo.tests;

import com.todo.BaseTest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class ToDoListTest extends BaseTest {

    private final String appName = "todo-list";
    private String currentRunPath;
    private int screenShotCounter = 0;

    @BeforeSuite
    public void beforeSuite() {
        setupBeforeSuite(appName);
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        setupBeforeMethod(method, true);
        screenShotCounter = 0;
    }

    @Test(description = "Add to cart")
    public void addToList() {
        String url = "http://localhost:3000/";
        driver.get(url);
        waitFor(5);
        waitForElementToBeVisible(By.id("newTask"));
        takeScreenShot("onLoad");
        eyes.checkWindow("onLoad");

        waitFor(5);
        String itemToAdd = "one more thing to do";
        waitForElementToBeVisible(By.id("newTask")).sendKeys(itemToAdd);
        takeScreenShot("itemAdded");
        eyes.checkWindow("itemAdded");

        waitFor(5);
        driver.findElement(By.id("addTask")).click();
        takeScreenShot("clickedOnAddTask");
        waitFor(10);
        eyes.checkWindow("clickedOnAddTask");
        takeScreenShot("afterWait");
    }

    private WebElement waitForElementToBeClickable(By element) {
        long l = 10;
        Duration timeout = Duration.ofSeconds(l);
        return new WebDriverWait(driver, timeout).until(ExpectedConditions.elementToBeClickable(element));
    }

    private WebElement waitForElementToBeVisible(By element) {
        long l = 10;
        Duration timeout = Duration.ofSeconds(l);
        return new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(element));
    }

    private void takeScreenShot(String fileName) {
        if (null != driver) {
            String directoryPath = System.getProperty("user.dir");
            File destinationFile = createScreenshotFile(directoryPath, browserType + "-" + fileName);
            try {
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(screenshot, destinationFile);
                System.out.println("Saved screenshot: " + destinationFile.getAbsolutePath());
            } catch (IOException | RuntimeException e) {
                System.out.println("ERROR: Unable to save or upload screenshot: '" + destinationFile.getAbsolutePath() + "' or upload screenshot to ReportPortal\n");
                System.out.println(ExceptionUtils.getStackTrace(e));
            }
        } else {
            System.out.println("Driver is not instantiated for this test");
        }
    }

    private File createScreenshotFile(String dirName, String fileName) {
        fileName = fileName.endsWith(".png") ? fileName : fileName + ".png";
        fileName = screenShotCounter++ + "-" + fileName;
        return new File(dirName + File.separator + "target" + File.separator + getUniquePath() + File.separator + fileName);
    }

    private String getUniquePath() {
        currentRunPath = (null == currentRunPath) ? getCurrentDatestamp() + File.separator + getCurrentTimestamp() + File.separator : currentRunPath;
        return currentRunPath;
    }

    static String getCurrentDatestamp() {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        return df.format(today);
    }

    static String getCurrentTimestamp() {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH-mm-ss");
        return df.format(today);
    }
}
