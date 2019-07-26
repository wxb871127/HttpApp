package http.com.testhttp;

//import http.com.httpannotation.Test;

//import com.httpplugin.Linhao;

//import http.com.httpannotation.Test;

//import http.com.httpannotation.Test;

//import com.httpplugin.Linhao;

public class GitHubRepo {
    private int id;
    private String name;

    public GitHubRepo() {
    }

    public int getId() {
        return id;
    }

//    @Linhao
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "id = " + id + ",name = " + name;
    }
}
