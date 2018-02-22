package com.williamhill.testing.cucumber.pages.common;

import com.williamhill.testing.cucumber.pages.PageObject;

public abstract class BasePage extends PageObject {
    private final Header header = new Header();
    private final LeftSidebar leftSidebar =  new LeftSidebar();

    public Header getHeader() {
        return header;
    }

    public LeftSidebar getLeftSidebar() {
        return leftSidebar;
    }
}
