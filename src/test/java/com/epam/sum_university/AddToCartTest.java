package com.epam.sum_university;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class AddToCartTest {
    @Test
    public void addToCartTest() {
        System.setProperty(
            "webdriver.chrome.driver",
            "/Users/Tetianka/IdeaProjects/Home_task_3/src/test/resources/webdriver/chromedriver"
        );
        WebDriver driver = new ChromeDriver();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        driver.get("https://www.amazon.com/");

        addProductToCart(driver);

        driver.close();
        driver.quit();
    }

    @Test
    public void removeFromCartTest() {
        System.setProperty(
                "webdriver.chrome.driver",
                "/Users/Tetianka/IdeaProjects/Home_task_3/src/test/resources/webdriver/chromedriver"
        );
        WebDriver driver = new ChromeDriver();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        driver.get("https://www.amazon.com/");

        String firstProductName = addProductToCart(driver);

        WebElement goToCart = driver.findElement(By.xpath("//*[@id=\"sw-gtc\"]/span/a"));
        goToCart.click();

        List<WebElement> cartProductNames = driver.findElements(By.xpath("//*[@id]/div[4]/div/div[1]/div/div/div[2]/ul/li[1]/span/a/span[1]/span/span[1]"));
        Optional<WebElement> foundProduct = cartProductNames.stream()
                .filter(cartProd -> cartProd.getText().toLowerCase(Locale.ROOT).equals(firstProductName))
                .findFirst();

        Assert.assertTrue(foundProduct.isPresent(), "Product should be in cart");

        WebElement deleteButton = driver.findElement(By.xpath("//*[@id]/div[4]/div/div[1]/div/div/div[2]/div[1]/span[2]/span/input"));
        deleteButton.click();

        WebElement emptyCartMessage = driver.findElement(By.xpath("//*[contains(text(),'Your Amazon Cart is empty')]"));
        WebElement totalPrice = driver.findElement(By.xpath("//*[@id=\"sc-subtotal-amount-activecart\"]/span"));

        Assert.assertTrue(emptyCartMessage.isDisplayed(), "Cart should be empty");
        Assert.assertEquals(totalPrice.getText(), "$0.00", "Total price should be zero");

        driver.close();
        driver.quit();
    }

    private String addProductToCart(WebDriver driver) {
        WebElement burgerMenu = driver.findElement(By.xpath("//*[@id=\"nav-hamburger-menu\"]"));
        burgerMenu.click();

        WebElement firstLink = driver.findElement(By.cssSelector("#hmenu-content ul li.hmenu-separator + li + li a"));
        firstLink.click();

        List<WebElement> subLinks = driver.findElements(By.xpath("//*[@id=\"hmenu-content\"]/ul[5]/li/a"));
        subLinks.get(2).click();

        List<WebElement> productNames = driver.findElements(By.xpath("//*[@id=\"search\"]/div[1]/div[1]/div/span[3]/div[2]/div/div/div/div/div/div/div/div[2]/div[1]/h2/a/span"));
        WebElement firstProduct = productNames.get(0);
        String firstProductName = firstProduct.getText().toLowerCase(Locale.ROOT);
        firstProduct.click();

        WebElement addToCartButton = driver.findElement(By.xpath("//*[@id=\"add-to-cart-button\"]"));
        addToCartButton.click();

        WebElement cartCounter = driver.findElement(By.xpath("//*[@id=\"nav-cart-count\"]"));
        Assert.assertEquals(cartCounter.getText(), "1", "Product should be added to cart");

        return firstProductName;
    }

}
