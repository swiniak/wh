package com.williamhill.testing.cucumber.pages;

import com.williamhill.testing.cucumber.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;

import javax.swing.text.html.Option;
import java.util.Optional;

public abstract class PageObject {

    protected final WebDriver driver;

    protected PageObject() {
        this(Utils.getInstance().getCurrentDriver());
    }

    protected PageObject(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    protected WebDriver getDriver() {
        return this.driver;
    }

    // The idea with these two was to run them before any other method call
    // and that classes extending PageObject could override it
    // to wait for specific conditions before trying to get anything out of particular page object.
    // I guess that could be done with InvocationHandler, but I didn't finish it. Shame on me.
    public static void waitForMe() {
    }

}
