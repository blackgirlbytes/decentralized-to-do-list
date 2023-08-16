package com.anandbagmar.ultrafastgrid.tests;

import com.anandbagmar.ultrafastgrid.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class ApplitoolsShoppingTest extends BaseTest {

    private final String appName = "applitools-shopping";

    @BeforeSuite
    public void beforeSuite() {
        setupBeforeSuite(appName);
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        setupBeforeMethod(method, true);
    }

    @Test(description = "Add to cart")
    public void addToCart() {
        String url = "https://demo.applitools.com/tlcHackathonMasterV1.html";
        driver.get(url);
        eyes.checkWindow("onLoad");
        driver.findElement(By.id("product_1")).click();

        eyes.checkWindow("product_1");
        if (isInject()) {
            ((JavascriptExecutor) driver).executeScript("document.querySelector(\"div#DIV__btnaddtoca__113\").setAttribute(\"id\", \"foo\")");
        }
        driver.findElement(By.id("DIV__btnaddtoca__113")).click();
        eyes.checkWindow("add to cart");
    }

    /* implement a new test to remove items from cart */

    // implement a new test to clear items from the cart

}
