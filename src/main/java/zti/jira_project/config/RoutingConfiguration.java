package zti.jira_project.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoutingConfiguration {
    @GetMapping("/")
    public String getIndex() {
        return "index.html";
    }

    @GetMapping("/register")
    public String getRegister() {
        return "register.html";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login.html";
    }

    @GetMapping("/tasksList")
    public String getTasks() {
        return "tasksList.html";
    }

    @GetMapping("/navbar")
    public String getNavbar() {
        return "shared/navbar.html";
    }
}
