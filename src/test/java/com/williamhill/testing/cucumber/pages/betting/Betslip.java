package com.williamhill.testing.cucumber.pages.betting;

import com.williamhill.testing.cucumber.Utils;
import com.williamhill.testing.cucumber.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.math.BigDecimal;

public class Betslip extends PageObject{

    private SingleBet singleBet = new SingleBet(); // TODO this should be map or array but it's easier that way for now
    //private static final String rootElementCssLocator = "div.betslip-footer__totals";

    public SingleBet getSingleBet() {
        return  singleBet;
    }

    public BigDecimal getTotalStake() {
        return Utils.stringToBigDecimal(driver.findElement(By.cssSelector("#total-stake-price")).getText());
    }

    public BigDecimal getToReturn() {
        return Utils.stringToBigDecimal(driver.findElement(By.cssSelector("#total-to-return-price")).getText());
    }

    public boolean placeBet() {
        driver.findElement(By.cssSelector("input[data-ng-click='placeBet()']")).click();
        Utils.waitForJSandJQueryToLoad();
        WebElement notification = Utils.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.id("receipt-notice-box")));
        return notification.getAttribute("class").contains("betslip-notification--success");
    }
}
