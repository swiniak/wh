package com.williamhill.testing.cucumber.entities.config;

import com.williamhill.testing.cucumber.Utils;
import com.williamhill.testing.cucumber.pages.PageObject;

import java.util.Optional;
import java.util.regex.Pattern;

public class Page {
    private String name;
    private String path;
    private String urlPattern;
    private String className;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean matches(String url) {
        String regex = getUrlPattern();
        if (regex.startsWith("/") || regex.startsWith("^/")) { // silly assumption, most likely will cause trouble later
            url = url.replace(Utils.getConfiguration().baseUrl, "");
        }
        return Pattern.compile(regex).matcher(url).matches();
    }

    public Optional<PageObject> getPageInstance() {
        try {
            Class clazz = Class.forName(this.className);
            PageObject instance = (PageObject) clazz.newInstance();
            return Optional.of(instance);
        } catch (Exception e) {
            // don't throw up, swallow exception
            // so yeah, TODO
            return Optional.empty();
        }
    }
}
