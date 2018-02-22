package com.williamhill.testing.cucumber.pages.betting;

import com.williamhill.testing.cucumber.Utils;
import com.williamhill.testing.cucumber.pages.PageObject;
import com.williamhill.testing.cucumber.pages.interfaces.IEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FootballEvent extends PageObject implements IEvent {
    /**
     * About this class:
     * https://i.imgur.com/FNn87RO.jpg
     */
    private static final String locatorFormat = "button[data-player='%s']";
    private String rootElementCssLocator;

    public FootballEvent(String rootElementCssLocator) {
        super();
        this.rootElementCssLocator = rootElementCssLocator;
    }

    public enum Side {
        HOME(0),
        AWAY(1),
        DRAW(-1);

        private Integer stringIndex;

        private Side(Integer index) {
            stringIndex = index;
        }

        public Integer getStringIndex() {
            return stringIndex;
        }
    }

    private WebElement getRootElement() {
        return getRootElement(0);
    }

    private WebElement getRootElement(int index) {
        return driver.findElements(By.cssSelector(rootElementCssLocator)).get(index);
    }

    public Date getStartTime() throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat();
        return df.parse(getRootElement().findElement(By.cssSelector("time[data-type='event-starttime']")).getAttribute("datetime"));
    }

    public String getOdds(Side side) {
        return getRootElement().findElement(By.cssSelector(String.format(locatorFormat, getSideName(side)))).getText();
    }

    public BigDecimal getOddsAsDecimal(Side side) {
        BigDecimal dividend = new BigDecimal(getOdds(side).split("/")[0]);
        BigDecimal divisor = new BigDecimal(getOdds(side).split("/")[1]);
        return dividend.divide(divisor);
    }

    public String getMarketName() {
        return getRootElement().findElement(By.cssSelector("ul.btmarket__content-margins a")).getAttribute("title");
    }

    public String getSideName(Side side) {
        if (Side.DRAW.equals(side)) {
            throw new RuntimeException(String.format("You know that %s is not a side, right?", side.name()));
        }
        return Utils.forceGetText(getRootElement().findElements(By.cssSelector("ul.btmarket__content-margins a span")).get(side.getStringIndex()));
    }

    public void addToBetslip(Side side) {
        getRootElement().findElement(By.cssSelector(String.format(locatorFormat, getSideName(side)))).click();
        Utils.waitForJSandJQueryToLoad();
    }


    @Override
    public Map<SingleBet.BetslipFields, String> getBetslipData() {
        Map<SingleBet.BetslipFields, String> data =  new HashMap<SingleBet.BetslipFields, String>();
        // TODO hardcoded side
        data.put(SingleBet.BetslipFields.ODDS, getOdds(Side.HOME));
        data.put(SingleBet.BetslipFields.SIDE_NAME, getSideName(Side.HOME));
        return data;
    }

}
