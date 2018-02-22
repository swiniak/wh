package com.williamhill.testing.cucumber.pages.betting;

import com.williamhill.testing.cucumber.Utils;
import com.williamhill.testing.cucumber.pages.PageObject;
import com.williamhill.testing.cucumber.pages.interfaces.IEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OpenBet extends PageObject implements IEvent {

    public BigDecimal getToReturn() {
        return Utils.stringToBigDecimal(driver.findElement(By.xpath("//div[contains(@id,'cashout-returns')]/span")).getText());
    }

    private String getBetDescription() {
        return driver.findElement(By.className("betslip-placed-bet__description")).getText();
    }

    public String getSideName() {
        return getBetDescription().split("@")[0].trim();
    }

    public String getOdds() {
        return getBetDescription().split("@")[1].trim();
    }

    @Override
    public Map<SingleBet.BetslipFields, String> getBetslipData() {
        Map<SingleBet.BetslipFields, String> data =  new HashMap<SingleBet.BetslipFields, String>();
        data.put(SingleBet.BetslipFields.ODDS, getOdds());
        data.put(SingleBet.BetslipFields.SIDE_NAME, getSideName());
        data.put(SingleBet.BetslipFields.ESTIMATED_RETURNS, getToReturn().setScale(2, BigDecimal.ROUND_DOWN).toString());
        return data;
    }

    public static void waitForMe() {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        Utils.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.className("betslip-placed-bet__returns-amount")));
    }
}
