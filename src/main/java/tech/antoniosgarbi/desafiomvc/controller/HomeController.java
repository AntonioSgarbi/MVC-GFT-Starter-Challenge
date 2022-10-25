package tech.antoniosgarbi.desafiomvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import tech.antoniosgarbi.desafiomvc.service.EventService;

@Controller
@RequestMapping("/")
public class HomeController {
    private final EventService eventService;

    public HomeController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("index.html");
        mv.addObject("events", this.eventService.findAll());

        return mv;
    }
}
