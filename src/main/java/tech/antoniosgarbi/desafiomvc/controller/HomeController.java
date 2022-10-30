package tech.antoniosgarbi.desafiomvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import tech.antoniosgarbi.desafiomvc.service.EventService;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {
    private final EventService eventService;

    @GetMapping
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("index.html");
        mv.addObject("events", this.eventService.findAll());

        return mv;
    }
}
