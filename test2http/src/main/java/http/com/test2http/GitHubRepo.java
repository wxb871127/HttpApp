package http.com.test2http;

//import http.com.httpannotation.Test;

//import com.httplib.Test;

//import http.com.httpannotation.Test;

public class GitHubRepo {
    private int id;
    private String name;
    private String xb;

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
