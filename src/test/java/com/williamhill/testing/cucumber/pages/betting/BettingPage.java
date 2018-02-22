package com.williamhill.testing.cucumber.pages.betting;

import com.williamhill.testing.cucumber.Utils;
import com.williamhill.testing.cucumber.pages.common.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.security.Key;
import java.util.List;

public class BettingPage extends BasePage {
    private Betslip betslip = new Betslip();
    private OpenBet openBet = new OpenBet();

    public OpenBet getOpenBet() {
        return openBet;
    }

    public Betslip getBetslip() {
        return betslip;
    }

    public enum Tab {
        BETSLIP("betslip-tab"),
        OPEN_BETS("openbets-tab");

        private String id;

        private Tab(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    private enum AlphabeticalOrderSection {
        AD,
        EL,
        MR,
        SZ;

        private static final String locatorFormat = "li[data-id=\"%s-%s\"]";
        private static final String dataRootLocatorFormat = "li[data-toggle-receive=\"%s-%s-\"]";
        private static final String dataRegionLocatorFormat = "li[data-region-id=\"%s-%s\"]";
        private char rangeStart;
        private char rangeEnd;

        private AlphabeticalOrderSection() {
            char[] chars = this.name().toUpperCase().toCharArray();
            rangeStart = chars[0];
            rangeEnd = chars[1];
        }


        public String getSectionLocator() {
            return String.format(locatorFormat, this.rangeStart, this.rangeEnd);
        }

        public String getDataRootLocator() {
            return String.format(dataRootLocatorFormat, this.rangeStart, this.rangeEnd);
        }

        public String getDataRegionLocator() {
            return String.format(dataRegionLocatorFormat, this.rangeStart, this.rangeEnd);
        }

        public static AlphabeticalOrderSection getForString(String searchString) {
            char firstLetter = searchString.toUpperCase().charAt(0);
            for(AlphabeticalOrderSection section : AlphabeticalOrderSection.values()) {
                if(firstLetter >= section.rangeStart && firstLetter <= section.rangeEnd) {
                    return section;
                }
            }
            throw new RuntimeException("Unable to find matching section");
        }
    }

    private AlphabeticalOrderSection currentSection;


    // I was running out of time when writing the part below.. sorry.


    public void findCompetition(String name) {
        AlphabeticalOrderSection section = AlphabeticalOrderSection.getForString(name);
        Utils.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(section.getSectionLocator()))).click(); // Actually should check if expanded etc.
        Utils.waitForJSandJQueryToLoad();
        driver.findElement(By.cssSelector(section.getDataRootLocator())).findElement(By.linkText(name)).click();
        Utils.waitForJSandJQueryToLoad();
        currentSection = section;
    }

    public FootballEvent betOnFirstEventHomeWin() {
        /** Unfortunately I cannot spend more time on the task but if I could I would parametrize the method to allow
         * selection of specified event from the list. Also I would probably create kind of table interface and class
         * implementing it for this particular table so that I could enumerate columns, get specific row by column
         * value, etc.
         */
        FootballEvent event = new FootballEvent(String.format("%s div.event", currentSection.getDataRegionLocator()));
        event.addToBetslip(FootballEvent.Side.HOME);
        return event;
    }

    public void switchTab(Tab tab) {
        driver.findElement(By.id(tab.getId())).findElement(By.tagName("a")).click();
        Utils.waitForJSandJQueryToLoad();
        // below is a workaround for incomplete implementation, move along, nothing to see here
        if(Tab.OPEN_BETS == tab) {
            List<WebElement> alertButtons = driver.findElements(By.xpath("//div[@id='cimb-alert']//input[@type='button']"));
            if(alertButtons.size() > 0) {
                alertButtons.get(0).click();
                Utils.waitForJSandJQueryToLoad();
            }
            OpenBet.waitForMe();
        }
    }
}
