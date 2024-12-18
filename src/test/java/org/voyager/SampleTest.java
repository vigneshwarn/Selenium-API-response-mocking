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
import java.util.Set;
import java.util.stream.Collectors;

public class SampleTest {
    static WebDriver driver;
    static final int timeoutInSeconds = 10;

    public static void main(String...arg) throws MockerException {
        networkLogMocking();
        testMocking();
    }

    private static void networkLogMocking() {
        driver = new ChromeDriver();
        NetworkLogUtils networkLogUtils = new NetworkLogUtils(driver);
        try {
            driver.get("https://www.kia.com/in/buy/find-a-dealer.html");
            waitForClickable(By.cssSelector("#select-state-button")).ifPresent(WebElement::click);
            waitForClickables(By.cssSelector("li[class*='ui-menu-item']"));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            Set<NetworkLog> networkLogs = networkLogUtils
                    .getNetworkLogMap()
                    .values()
                    .stream()
                    .filter(networkLog -> networkLog.getApiUrl().contains("https://www.kia.com/api"))
                    .collect(Collectors.toSet());
            System.out.println(networkLogUtils.getNetworkLogMap().size());
            System.out.println(networkLogs
                    .stream()
                    .map(networkLog -> String.format("ApiUrl : %s,\nApiMethod : %s,\nRequestId : %s,\nStartTimeStamp : %s,\nUiUrl : %s,\n" +
                                    "EndTimeStamp : %s,\nDownloadedData : %s,\nRequestHeader : %s,\nResponseHeader : %s,\n" +
                                    "ResponseBody : %s,\n\n",
                            networkLog.getApiUrl(), networkLog.getApiMethod(), networkLog.getRequestId(),
                            networkLog.getStartTimeStamp(), networkLog.getUiUrl(), networkLog.getEndTimeStamp(), networkLog.getDownloadedData(),
                            networkLog.getRequestHeader(), networkLog.getResponseHeader(), networkLog.getResponseBody()))
                    .collect(Collectors.joining(",\n")));
            networkLogUtils.stopNetworkLog();
            driver.quit();
        }
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
