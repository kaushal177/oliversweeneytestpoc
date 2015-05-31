package com;

import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

/**
 * Created by kaveesh on 31/05/2015.
 */
public class SearchTest {
    static WebDriver driver;

    @BeforeClass
    public static void before() {
        // driver = new FirefoxDriver();
      //  System.setProperty("webdriver.chrome.driver", "/Users/kaveesh/Desktop/Interview Stuff/chromedriver.exe");
        driver = new FirefoxDriver();
        driver.get("https://www.oliversweeney.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void after() {
        driver.quit();
        driver = null;
    }

    @Test
    public void userCanSearchItem() {
        driver.findElement(By.name("search")).clear();
        driver.findElement(By.name("search")).sendKeys("shirt");
        driver.findElement(By.name("search")).sendKeys(Keys.RETURN);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<WebElement> listOfShirts = driver.findElements(By.xpath(".//*[@id='products-container']//div/a"));
        Assert.assertTrue(!listOfShirts.isEmpty());

        WebElement firstShirtResult = listOfShirts.get(0);
        String nameOfShirt = firstShirtResult.getAttribute("title");
        firstShirtResult.click();
        String pdpShirtName = waitForExpectedElement(By.cssSelector(".short-product-name")).getText();
        String pdpAttribute = waitForExpectedElement(By.cssSelector(".product-name")).getText();

        Assert.assertEquals(nameOfShirt.toLowerCase(), pdpShirtName.toLowerCase());
        Assert.assertTrue(pdpAttribute.toLowerCase().contains("shirt"));
    }


    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {

        boolean acceptNextAlert = true;
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.dismiss();
            } else {
                alert.accept();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }


    public WebElement waitForExpectedElement(final By by) {

        WebDriverWait wait = new WebDriverWait(driver, 10);
        return wait.until(visibilityOfElementLocated(by));
    }


    public WebElement waitForExpectedElement(final By by, long waitTimeInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            return wait.until(visibilityOfElementLocated(by));
        } catch (NoSuchElementException e) {
            //  LOG.info(e.getMessage());
            return null;
        } catch (TimeoutException e) {
            //LOG.info(e.getMessage());
            return null;
        }
    }


    protected ExpectedCondition<WebElement> visibilityOfElementLocated(final By by) throws NoSuchElementException {
        return new ExpectedCondition<WebElement>() {


            public WebElement apply(WebDriver driver) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    //      LOG.error(e.getMessage());
                }
                WebElement element = driver.findElement(by);
                return element.isDisplayed() ? element : null;
            }
        };
    }

}
