package tech.antoniosgarbi.desafiomvc.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tech.antoniosgarbi.desafiomvc.model.Activity;
import tech.antoniosgarbi.desafiomvc.model.Participant;
import tech.antoniosgarbi.desafiomvc.service.ActivityService;
import tech.antoniosgarbi.desafiomvc.service.EventService;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    private final ActivityService activityService;
    private final EventService eventService;

    public ActivityController(ActivityService activityService, EventService eventService) {
        this.activityService = activityService;
        this.eventService = eventService;
    }

    @GetMapping("/presence-list")
    public ModelAndView getPresenceList(@RequestParam Long id, @RequestParam Long eventId) {
        ModelAndView mv = new ModelAndView("/activity/activity_delivered.html");

        Activity activity = this.activityService.findById(id);
        List<Participant> participants = this.eventService.findAllParticipantsFromEvent(eventId);

        mv.addObject("eventId", eventId);
        mv.addObject("activity", activity);
        mv.addObject("participants", participants);

        return mv;
    }  
    
    @PostMapping("/presence-list")
    public ModelAndView getPresenceList(@Valid Activity activity, @RequestParam Long eventId, BindingResult bindingResult) {
        
        ModelAndView mv = new ModelAndView("/activity/activity_delivered.html");

        if(bindingResult.hasErrors()) {
            mv.addObject("message", "error");
            return mv;
        }

        try {
            Activity saved = this.activityService.save(activity);
            List<Participant> participants = this.eventService.findAllParticipantsFromEvent(eventId);

            mv.addObject("eventId", eventId);
            mv.addObject("activity", saved);
            mv.addObject("participants", participants);
            mv.addObject("message", "Lista atualizada com Sucesso!");
            mv.addObject("updated", "true");

        } catch(Exception e) {
            mv.addObject("error", e.getMessage());

        } 
        return mv;
    }


}
