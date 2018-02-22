package com.williamhill.testing.cucumber.steps;

import com.williamhill.testing.cucumber.entities.config.User;
import com.williamhill.testing.cucumber.pages.betting.SingleBet;
import com.williamhill.testing.cucumber.pages.betting.BettingPage;
import com.williamhill.testing.cucumber.pages.betting.FootballEvent;
import com.williamhill.testing.cucumber.pages.common.BasePage;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;

public class World {
    public WebDriver driver;
    public User currentUser;
    public BasePage basePage;
    public BettingPage bettingPage;
    public FootballEvent footballEvent;
    public SingleBet singleBet;
    public BigDecimal stake; // TODO this shouldn't be here
}
