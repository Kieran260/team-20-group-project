package com.iamin.data.validation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import java.time.Duration;

public class LoginViewTest {
    private WebDriver driver;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "/Users/kieranhardwick/Downloads/chromedriver_mac_arm64/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
    }

    @Test
    public void testLoginView() throws InterruptedException {
        Thread.sleep(5000); // Wait for 5 seconds

        // Navigate to your application
        driver.get("localhost:8000/login");

        // Wait until the elements are present
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));



        
        // Test elements on the login view
        WebElement registerButton = wait.until(presenceOfElementLocated(By.id("turn-to-register-button")));
        assertEquals("Register", registerButton.getText());

        // Click register button to switch to the sign up view
        registerButton.click();

        
    }

    @AfterEach
    public void teardown() {
        // Close the browser after running the test
        if (driver != null) {
            driver.quit();
        }
    }
}

