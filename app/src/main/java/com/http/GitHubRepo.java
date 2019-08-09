package com.http;

public class GitHubRepo {
    private int id;
    private String name;

    public GitHubRepo() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "id = " + id + ",name = " + name;
    }
}
