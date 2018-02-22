package com.williamhill.testing.cucumber.entities.config;

import java.util.List;

public class Data {

    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getUserByName(String name) {
        return users
                .stream()
                .filter(u -> u.getName().equalsIgnoreCase(name))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
