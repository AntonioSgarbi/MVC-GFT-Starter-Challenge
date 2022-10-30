package tech.antoniosgarbi.desafiomvc.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import tech.antoniosgarbi.desafiomvc.model.Event;
import tech.antoniosgarbi.desafiomvc.model.Group;
import tech.antoniosgarbi.desafiomvc.service.EventService;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {
    private final EventService eventService;

    @GetMapping
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("ranking/events.html");
        mv.addObject("events", this.eventService.findAll());

        return mv;
    }

    @GetMapping("/ranking/event")
    public ModelAndView ranking(@RequestParam Long eventId, @RequestParam(required = false) boolean fromWinner) {
        ModelAndView mv = new ModelAndView("/ranking/ranking.html");

        Event event = this.eventService.findById(eventId);

        Collections.sort(event.getGroups(),
                Comparator
                        .comparingInt(Group::getScore)
                        .reversed()
                        .thenComparing(Group::getName));

        Date date = new Date();

        if (event.getEnd().compareTo(date) < 0 && !fromWinner) {
            mv = new ModelAndView("/ranking/winner.html");
        }

        mv.addObject("event", event);

        return mv;
    }
}
