package com.anandbagmar.ultrafastgrid.tests;

import com.anandbagmar.ultrafastgrid.BaseTest;
import com.applitools.eyes.selenium.Eyes;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class GitHubTest extends BaseTest {
    private final String appName = "githubLogin";

    @BeforeSuite
    public void beforeSuite() {
        setupBeforeSuite(appName);
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        setupBeforeMethod(method, true);
    }

    @Test(description = "Login to Github")
    public void loginNoCreds() {
        String url = "https://github.com/login";
        driver.get(url);
        eyes.checkWindow("onLoad");
        if (isInject()) {
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"input.btn\").setAttribute(\"value\",\"Sign\")");
        }
        eyes.checkWindow("loginPage");
        driver.findElement(By.xpath("//input[@value=\"Sign in\"]")).click();
        eyes.checkWindow("loginErrors");
    }
}
