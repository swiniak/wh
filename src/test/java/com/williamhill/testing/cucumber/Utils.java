package com.williamhill.testing.cucumber;

import com.williamhill.testing.cucumber.entities.config.Configuration;
import com.williamhill.testing.cucumber.entities.config.Page;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.validator.routines.UrlValidator;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Utils {

    private static final Utils instance = new Utils();
    private final UrlValidator urlValidator;
    private Configuration configuration;
    /* I guess it could be nice to add Map<String,WebDriver> that could hold more than one instance
       (e.g. to test simultaneous bets by same user account)
       and some closeAll method to cleanup
     */
    private WebDriver currentDriver;

    private Utils() {
        urlValidator = new UrlValidator();
        Constructor constructor = new Constructor(Configuration.class);//Car.class is root
        TypeDescription configurationDescription = new TypeDescription(Configuration.class);
        constructor.addTypeDescription(configurationDescription);
        Yaml yamlConfig = new Yaml(constructor);
        try {
            // TODO hardcoded path
            InputStream input = new FileInputStream(new File("src/test/resources/config/default.yml"));
            configuration = yamlConfig.load(input);
        } catch (FileNotFoundException e) {
            // TODO
            e.printStackTrace();
        }
    }

    public static Utils getInstance() {
        return instance;
    }

    public static Configuration getConfiguration() {
        return getInstance().configuration;
    }

    public static boolean isValidUrl(String url) {
        return getInstance().urlValidator.isValid(url);
    }

    public static Optional<Page> navigateToPage(String pageName) {
        return navigateToRelative(findPageByName(pageName));
    }

    public static Optional<Page> navigateToRelative(Page page) {
        return navigateToRelative(page.getPath());
    }

    public static Optional<Page> navigateToRelative(String path) {
        getInstance().getCurrentDriver().navigate().to(getConfiguration().getBaseUrl() + path);
        waitForJSandJQueryToLoad();
        return whereAmI();
    }

    public static Optional<Page> whereAmI() {
        WebDriver driver = getInstance().getCurrentDriver();
        String currentUrl = driver.getCurrentUrl();
        Supplier<Stream<Page>> streamSupplier = () -> getConfiguration().getPages()
                .stream()
                .filter(p -> p.matches(currentUrl));
        if (streamSupplier.get().count() > 1) {
            throw new RuntimeException("More than url matches this pattern.");
        }
        return streamSupplier.get().findFirst();
    }

    public static WebDriverWait getWait() {
        return getWait(Optional.empty());
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static WebDriverWait getWait(Optional<Integer> timeoutInSeconds) {
        //noinspection OptionalIsPresent
        if (timeoutInSeconds.isPresent()) {
            return new WebDriverWait(getInstance().getCurrentDriver(), timeoutInSeconds.get());
        }
        return new WebDriverWait(getInstance().getCurrentDriver(), getConfiguration().getWaitTimeout());
    }

    public static Page findPageByName(String name) {
        return getConfiguration().getPages()
                .stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static BigDecimal calculateReturn(BigDecimal stake, BigDecimal odds) {
        return stake.multiply(odds).add(stake).setScale(2, BigDecimal.ROUND_DOWN);
    }

    public static BigDecimal stringToBigDecimal(String string) {
        return new BigDecimal(string.replaceAll("[^\\d.,]", ""));
    }

    public static String forceGetText(WebElement element) {
        return (String) ((JavascriptExecutor)getInstance().getCurrentDriver()).executeScript("return arguments[0].innerHTML", element);
    }

    public WebDriver getCurrentDriver() {
        if (currentDriver == null) {
            Class<? extends WebDriver> driverClass = null;
            try {
                driverClass = (Class<? extends WebDriver>)Class.forName(configuration.getDriver());
            } catch (ClassNotFoundException e) {
                // TODO bad kitty
                e.printStackTrace();
            }
            WebDriverManager.getInstance(driverClass).setup();
            try {
                currentDriver = driverClass.newInstance();
                currentDriver.manage().window().maximize(); // I consider this hacky but my default window size
                                                            // displays mobile layout (which by the way should be supported by tests)
            } catch (Exception e) {
                // TODO
            }
        }
        return currentDriver;
    }

    public static boolean waitForJSandJQueryToLoad() {
        WebDriver driver = getInstance().getCurrentDriver();
        WebDriverWait wait = new WebDriverWait(driver, getConfiguration().getWaitTimeout());

        // wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long)((JavascriptExecutor)driver).executeScript("return jQuery.active") == 0);
                }
                catch (Exception e) {
                    // no jQuery present
                    return true;
                }
            }
        };

        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor)driver).executeScript("return document.readyState")
                        .toString().equals("complete");
            }
        };

        return wait.until(jsLoad) && wait.until(jQueryLoad);
    }
}
