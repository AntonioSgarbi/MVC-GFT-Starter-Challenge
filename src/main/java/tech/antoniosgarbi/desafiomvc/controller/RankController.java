package tech.antoniosgarbi.desafiomvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import tech.antoniosgarbi.desafiomvc.model.Event;
import tech.antoniosgarbi.desafiomvc.service.EventService;


@Controller
@RequestMapping("/rank")
public class RankController {
    private final EventService eventService;

    public RankController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ModelAndView loadRankFromEvent(@RequestParam Long eventId) {
        Event event = this.eventService.findById(eventId);


        return null;
    }

}
