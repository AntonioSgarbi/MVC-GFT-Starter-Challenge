package tech.antoniosgarbi.desafiomvc.controller;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import tech.antoniosgarbi.desafiomvc.config.UserDetailsServiceImpl;
import tech.antoniosgarbi.desafiomvc.model.UserModel;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public UserController(UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @GetMapping("/edit")
    public ModelAndView getForm() {
        ModelAndView mv = new ModelAndView("user/form.html");
        mv.addObject("user", new UserModel());
        return mv;
    }

    @PostMapping("/edit")
    public ModelAndView postForm(UserModel userModel, BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView("user/form.html");
        mv.addObject("user", userModel);
        
        String password = this.userDetailsServiceImpl.createUser(userModel);
        mv.addObject("message", "Usuário criado, senha: " + password);
        
        return mv;
    }
}