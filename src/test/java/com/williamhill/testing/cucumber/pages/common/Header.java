// It's not really a page, is it? I was thinking to make a separate package for 'sections'
package com.williamhill.testing.cucumber.pages.common;

import com.williamhill.testing.cucumber.Utils;
import com.williamhill.testing.cucumber.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.math.BigDecimal;
import java.util.Optional;

public class Header extends PageObject {

    private static final String accountMenuButtonLocator = "#accountTabButton .-account";

    private WebElement getLoginMenuButton() {
        return driver.findElement(By.cssSelector("#accountTabButton span[data-translate=SBHEADER_LOGIN]"));
    }

    private WebElement getAccountMenuButton() {
        return driver.findElement(By.cssSelector(accountMenuButtonLocator));
    }

    private WebElement getUserInput() {
        return driver.findElement(By.id("loginUsernameInput"));
    }

    private WebElement getPasswordInput() {
        return driver.findElement(By.id("loginPasswordInput"));
    }


    private WebElement getSubmitLoginButton() {
        return driver.findElement(By.cssSelector("button[data-automation-id=loginButton]"));
    }

    public void logIn(String userName, String password) {
        logIn(Optional.of(userName), password);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public void logIn(Optional<String> userName, String password) {
        // I've made Optional<String> userName to allow checking "Save username" functionality
        // although it could be checked in many other ways
        if (isLoggedIn()) {
            throw new RuntimeException("User is already logged in");
        }
        if (!isLoginPanelExpanded()) {
            toggleLoginPanel();
        }
        //noinspection OptionalIsPresent
        if (userName.isPresent()) {
            WebElement userInput = getUserInput();
            userInput.clear();
            userInput.sendKeys(userName.get());
        }
        getPasswordInput().sendKeys(password);
        getSubmitLoginButton().click();
        Utils.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(accountMenuButtonLocator)));
        Utils.waitForJSandJQueryToLoad();
    }

    private Optional<WebElement> getLoginPanelElement() {
        return driver.findElements(By.id("not-logged")).stream().findFirst();
    }

    public void toggleLoginPanel() {
        getLoginMenuButton().click();
    }

    public boolean isLoginPanelExpanded() {
        return getLoginPanelElement().isPresent() && getLoginPanelElement().get().isDisplayed();
    }

    public boolean isLoggedIn() {
        // TODO very naive assumptions
        return !getLoginMenuButton().isDisplayed() && getAccountMenuButton().isDisplayed();
    }

    public BigDecimal getAvailableFunds() {
        return Utils.stringToBigDecimal(getAccountMenuButton().getText());
    }
}
