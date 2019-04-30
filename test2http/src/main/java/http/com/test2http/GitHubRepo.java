package http.com.test2http;

import http.com.httpannotation.Test;

public class GitHubRepo {
    private int id;
    private String name;

    public GitHubRepo() {
    }

    @Test("aaaa")
    public int getId() {
        return id;
    }

    @Test("aaa2")
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "id = " + id + ",name = " + name;
    }
}
