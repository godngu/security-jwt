package godngu.securityjwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/api/hello")
    public String apiHello() {
        return "apiHello";
    }

    @GetMapping("/api/hello/admin")
    public String roleAdmin() {
        return "roleAdmin";
    }

    @GetMapping("/api/hello/manager")
    public String roleManager() {
        return "roleManager";
    }

    @GetMapping("/api/hello/user")
    public String roleUser() {
        return "roleUser";
    }
}
