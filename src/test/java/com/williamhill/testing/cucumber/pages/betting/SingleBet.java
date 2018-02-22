package com.williamhill.testing.cucumber.pages.betting;

import com.williamhill.testing.cucumber.Utils;
import com.williamhill.testing.cucumber.pages.interfaces.IEvent;
import com.williamhill.testing.cucumber.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class SingleBet extends PageObject implements IEvent {

    /** TODO refactor - this class should also hold singleBet ID and behave a bit different
     * The idea is that it represents single bet only
     */

    private static String rootElementId = "betslip-content";

    public enum BetslipFields {
        MARKET_NAME(".//span[@class='betslip-selection__event']"),
        SIDE_NAME(".//span[@class='betslip-selection__name']/label[contains(@for,'stake-input_')]"),
        ODDS(".//span[@class='betslip-selection__name']//span[contains(@id,'bet-price_')]"),
        ESTIMATED_RETURNS(".//span[contains(@id,'estimated-returns_')]"),
        STAKE_INPUT(".//input[contains(@id,'stake-input__')]");

        private String xpath;

        private BetslipFields(String xpath) {
            this.xpath = xpath;
        }

        public String getXpath() {
            return xpath;
        }
    }

    private WebElement getRootElement() {
        return driver.findElement(By.id(rootElementId));
    }

    public String getSideName() {
        return getRootElement().findElement(By.xpath(BetslipFields.SIDE_NAME.getXpath())).getText();
    }

    public String getMarketName() {
        return getRootElement().findElement(By.xpath(BetslipFields.MARKET_NAME.getXpath())).getText();
    }

    public String getOdds() {
        return getRootElement().findElement(By.xpath(BetslipFields.ODDS.getXpath())).getText();
    }

    public BigDecimal getOddsAsDecimal() {
        BigDecimal dividend = new BigDecimal(getOdds().split("/")[0]);
        BigDecimal divisor = new BigDecimal(getOdds().split("/")[1]);
        return dividend.divide(divisor);
    }

    public BigDecimal getEstimatedReturns() {
        return Utils.stringToBigDecimal(getRootElement().findElement(By.xpath(BetslipFields.ESTIMATED_RETURNS.getXpath())).getText());
    }

    public void setStake(BigDecimal stake) {
        WebElement input = getRootElement().findElement(By.xpath(BetslipFields.STAKE_INPUT.getXpath()));
        input.clear();
        input.sendKeys(stake.toString());
    }

    public BigDecimal getStake() {
        return Utils.stringToBigDecimal(getRootElement().findElement(By.xpath(BetslipFields.STAKE_INPUT.getXpath())).getAttribute("value"));
    }

    @Override
    public Map<BetslipFields, String> getBetslipData() {
        Map<BetslipFields, String> data = new HashMap<BetslipFields, String>();
        data.put(BetslipFields.SIDE_NAME, getSideName());
        data.put(BetslipFields.ODDS, getOdds());
        data.put(BetslipFields.ESTIMATED_RETURNS, getEstimatedReturns().setScale(2, BigDecimal.ROUND_DOWN).toString());
        data.put(BetslipFields.MARKET_NAME, getMarketName());
        return data;
    }

    public boolean matchesEventData(IEvent event) {
        Map<BetslipFields, String> eventData = event.getBetslipData();
        if(eventData.isEmpty()) {
            return false; // TODO exception might be better
        }
        Map<BetslipFields, String> betslipData = this.getBetslipData();
        for (Map.Entry<BetslipFields,String> pair : event.getBetslipData().entrySet()) {
            // TODO this is ugly
            if(!betslipData.get(pair.getKey()).trim().equals(pair.getValue().trim())) {
                return false;
            }
        }
        return true;
    }

    public BigDecimal calculateReturn() {
        return Utils.calculateReturn(getStake(), getOddsAsDecimal());
    }

}
