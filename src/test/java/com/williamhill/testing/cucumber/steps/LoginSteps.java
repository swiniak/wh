package com.williamhill.testing.cucumber.steps;

import com.williamhill.testing.cucumber.Utils;
import com.williamhill.testing.cucumber.entities.config.Page;
import com.williamhill.testing.cucumber.entities.config.User;
import com.williamhill.testing.cucumber.pages.PageObject;
import com.williamhill.testing.cucumber.pages.common.BasePage;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

import java.util.Optional;

import static junit.framework.Assert.assertTrue;

public class LoginSteps {

    private World world;

    public LoginSteps(World world) {
        this.world = world;
    }

    @Given("^(.*) has an account$")
    public void userHasAccount(String userName) {
        // I'd rather have this method to check if account is in the system
        world.currentUser = Utils.getConfiguration().getData().getUserByName(userName);
    }

    @Given("^(.*) (?:goes|navigates) to (.*)(?: page|(?:web)site)$")
    public void navigateTo(String user, String target) {
        if (Utils.isValidUrl(target)) {
            world.driver.navigate().to(target);
        }
//        WebElement searchField = driver.findElement(By.id("searchInput"));
//        searchField.sendKeys(searchTerm);
    }

    @Given("^he(?:'s| is) on (.*) page$")
    public void navigateTo(String pageName) {
        Utils.navigateToPage(pageName);
        Optional<Page> page = Utils.whereAmI();
        // I guess there are better asserts than boolean
        assertTrue(page.isPresent() && page.get().getName().equalsIgnoreCase(pageName));
    }


    @When("^s?he logs in$")
    public void login() {
        login(world.currentUser);
    }

    @When("^(.*)(?<!^he|she) logs in$")
    public void login(String userName) {
        login(Utils.getConfiguration().getData().getUserByName(userName));
    }

    private void login(User user) {
        Optional<Page> page = Utils.whereAmI();
        if (page.isPresent()) {
            Optional<PageObject> pageObject = page.get().getPageInstance();
            if (pageObject.isPresent() && pageObject.get() instanceof BasePage) {
                world.basePage = (BasePage) pageObject.get();
                world.basePage.getHeader().logIn(user.getUserName(), user.getPassword());
                return;
            }
        }
        throw new UnsupportedOperationException(
                String.format("Blimey %s! I have no idea how to login from where we are.", user.getName()));
    }

}

