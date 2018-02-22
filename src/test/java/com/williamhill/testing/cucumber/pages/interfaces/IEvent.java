package com.williamhill.testing.cucumber.pages.interfaces;

import com.williamhill.testing.cucumber.pages.betting.SingleBet;

import java.util.Map;

public interface IEvent {

    public Map<SingleBet.BetslipFields, String> getBetslipData();
}
