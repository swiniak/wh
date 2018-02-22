package com.williamhill.testing.cucumber.steps;

import com.williamhill.testing.cucumber.Utils;
import com.williamhill.testing.cucumber.pages.betting.Betslip;
import com.williamhill.testing.cucumber.pages.betting.FootballEvent;
import com.williamhill.testing.cucumber.pages.betting.SingleBet;
import com.williamhill.testing.cucumber.pages.betting.BettingPage;
import com.williamhill.testing.cucumber.pages.common.LeftSidebar;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.math.BigDecimal;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


public class BettingSteps {

    private World world;

    public BettingSteps(World world) {
        this.world = world;
    }

    @Before
    public void before() {
        world.driver = Utils.getInstance().getCurrentDriver();
    }

    @After
    public void after() {
        world.driver.quit();
    }

    @Then("^(?:bob's your uncle, )?he(?:'s| is) logged in$")
    public void isLoggedIn() {
        assertTrue(world.basePage.getHeader().isLoggedIn());
    }

    @Given("^he finds (.*) ([^ ]+) event$")
    public void findEvent(String eventName, String eventCategory) {
        LeftSidebar sidebar = world.basePage.getLeftSidebar();
        //For some reason the following line did not work for me in Chrome. After clicking on nav-football I was brought
        //to football events but context navigation did not appear (so I could not select "competitions").
        // TODO Should be investigated and either fixed in tests or reported as bug for dev
        try {
            sidebar.goTo(eventCategory, LeftSidebar.Section.POPULAR);
            sidebar.goTo("competitions", LeftSidebar.Section.CONTEXT);
        }
        catch (Exception e) {
            Utils.navigateToRelative(String.format("%s/competitions", Utils.whereAmI().get().getPath()));
        }
        world.bettingPage = new BettingPage();
        world.bettingPage.findCompetition(eventName);
    }

    @Given("^he has at least (\\d+(?:\\.\\d+)?) on his account$")
    public void hasAmount(BigDecimal amount) {
        assertTrue(world.basePage.getHeader().getAvailableFunds().compareTo(amount) >= 0);
    }

    @When("^he bets (\\d+(?:\\.\\d+)?) on (.*) to win$")
    public void betMoneyOnWin(BigDecimal amount, String side) {
        world.footballEvent = world.bettingPage.betOnFirstEventHomeWin();
        Betslip betslip = world.bettingPage.getBetslip();
        SingleBet singleBet = betslip.getSingleBet();
        world.stake = amount;
        singleBet.setStake(amount);
        assertEquals(amount, singleBet.getStake());
        assertTrue(betslip.getSingleBet().matchesEventData(world.footballEvent));
        assertEquals(betslip.getTotalStake(), world.stake);
        BigDecimal expectedReturn = Utils.calculateReturn(world.stake, world.footballEvent.getOddsAsDecimal(FootballEvent.Side.HOME));
        assertEquals(betslip.getToReturn(), expectedReturn);
        assertEquals(betslip.getSingleBet().getEstimatedReturns(), expectedReturn);
        assertTrue(betslip.placeBet());
    }

    @Then("^he (?:nitpicks|checks) odds and returns$")
    public void pernickety() {
        world.bettingPage.switchTab(BettingPage.Tab.OPEN_BETS);
        assertTrue(world.bettingPage.getBetslip().getSingleBet().matchesEventData(world.bettingPage.getOpenBet()));
    }

}