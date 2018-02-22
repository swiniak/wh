package com.williamhill.testing.cucumber.pages.common;

import com.williamhill.testing.cucumber.Utils;
import com.williamhill.testing.cucumber.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import javax.rmi.CORBA.Util;
import java.security.Key;
import java.util.Optional;
import java.util.stream.Stream;

public class LeftSidebar extends PageObject{

    private static final String rootLocator = "#desktop-sidebar";
    private static final String navLocatorFormat = "#nav-%s";
    private String currentContext = "";

    public enum Section {
        POPULAR("#desktop-sidebar-quick-links"),
        ALL("#desktop-sidebar-az"),
        CONTEXT("#sidebar-left-context"),
        EXTRAS("#desktop-sidebar-extras");

        private String cssSelector;

        Section(String cssSelector) {
            this.cssSelector = cssSelector;
        }
    }

    private String getNavLocator(String name, Section section) {
        if(section.equals(Section.CONTEXT) && !currentContext.isEmpty()) {
            return String.format("%s %s", section.cssSelector, String.format(navLocatorFormat, currentContext + "-" + name));
        }
        return String.format("%s %s", section.cssSelector, String.format(navLocatorFormat, name));
    }

    private WebElement getNavItem(String name, Section section) {
        return driver.findElement(By.cssSelector(getNavLocator(name, section)));
    }

    public void goTo(String name) {
        goTo(name, Section.ALL);
    }

    public void forceCurrentContext(String currentContext) {
        this.currentContext = currentContext;
    }

    public void goTo(String name, Section section) {
        WebElement navItem = getNavItem(name, section);
        navItem.click();
        if(!section.equals(Section.CONTEXT)) {
            currentContext = "";
            Utils.getWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(getNavLocator(name, Section.CONTEXT))));
            currentContext = name;
        }
        if(!Utils.waitForJSandJQueryToLoad()) {
            throw new RuntimeException("Unable to complete navigation");
        }
    }

}
