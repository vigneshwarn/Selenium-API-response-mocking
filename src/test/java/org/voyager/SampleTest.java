package org.voyager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.voyager.exception.MockerException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SampleTest {
    static WebDriver driver;
    static final int timeoutInSeconds = 10;

    public static void main(String...arg) throws MockerException {
        testMocking();
    }

    private static void testMocking() throws MockerException {
        driver = new ChromeDriver();
        try {
            MockerUtils mockerUtils = new MockerUtils();
            mockerUtils.mockService((ChromeDriver) driver, "https://www.kia.com/api/kia2_in/findAdealer.getStateCity.do", "{\n" +
                    "  \"data\": {\n" +
                    "    \"stateAndCity\": [\n" +
                    "      {\n" +
                    "        \"val2\": [\n" +
                    "          {\n" +
                    "            \"value\": \"Port Blair\",\n" +
                    "            \"key\": \"S30\"\n" +
                    "          }\n" +
                    "        ],\n" +
                    "        \"val1\": {\n" +
                    "          \"value\": \"Ahamed Abdul Rahman\",\n" +
                    "          \"key\": \"AAR\"\n" +
                    "        }\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  \"resultCode\": \"0000\",\n" +
                    "  \"message\": \"\"\n" +
                    "}", 200);
            driver.get("https://www.kia.com/in/buy/find-a-dealer.html");
            waitForClickable(By.cssSelector("#select-state-button")).ifPresent(WebElement::click);
            String stateList = waitForClickables(By.cssSelector("li[class*='ui-menu-item']")).stream()
                    .skip(1)
                    .map(WebElement::getText)
                    .collect(Collectors.joining(","));
            if (stateList.equalsIgnoreCase("Ahamed Abdul Rahman")) {
                System.out.println("The text is matching : " + stateList + " == " + "Ahamed Abdul Rahman");
            } else {
                System.out.println("The text is not matching : " + stateList + " != " + "Ahamed Abdul Rahman");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    public static Optional<WebElement> waitForClickable(By by) {
        try {
            return Optional.ofNullable(new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                    .until(ExpectedConditions.elementToBeClickable(by)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public static List<WebElement> waitForClickables(By by) {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                    .until(ExpectedConditions.numberOfElementsToBeMoreThan(by, 1));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }
}
