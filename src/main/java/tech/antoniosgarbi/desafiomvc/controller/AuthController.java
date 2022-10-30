package tech.antoniosgarbi.desafiomvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login.html")
    public ModelAndView getForm() {
        return new ModelAndView("/auth/login.html");
    }

    @GetMapping("/login-error.html")
    public ModelAndView loginError() {
        ModelAndView mv =  new ModelAndView("/auth/login.html");
        return mv;
    }

}
