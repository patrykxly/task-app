package zti.jira_project.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling routing to various HTML pages.
 */
@Controller
public class RoutingConfiguration {

    /**
     * Maps the root URL ("/") to the index page.
     *
     * @return the name of the index HTML file
     */
    @GetMapping("/")
    public String getIndex() {
        return "index.html";
    }

    /**
     * Maps the "/register" URL to the register page.
     *
     * @return the name of the register HTML file
     */
    @GetMapping("/register")
    public String getRegister() {
        return "register.html";
    }

    /**
     * Maps the "/login" URL to the login page.
     *
     * @return the name of the login HTML file
     */
    @GetMapping("/login")
    public String getLogin() {
        return "login.html";
    }

    /**
     * Maps the "/tasksList" URL to the tasks list page.
     *
     * @return the name of the tasks list HTML file
     */
    @GetMapping("/tasksList")
    public String getTasks() {
        return "tasksList.html";
    }

    /**
     * Maps the "/navbar" URL to the shared navbar page.
     *
     * @return the name of the shared navbar HTML file
     */
    @GetMapping("/navbar")
    public String getNavbar() {
        return "fragments/navbar.html";
    }
}
