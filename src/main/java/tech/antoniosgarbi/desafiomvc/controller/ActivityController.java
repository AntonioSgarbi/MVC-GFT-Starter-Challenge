package tech.antoniosgarbi.desafiomvc.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tech.antoniosgarbi.desafiomvc.model.Activity;
import tech.antoniosgarbi.desafiomvc.model.Participant;
import tech.antoniosgarbi.desafiomvc.service.ActivityService;
import tech.antoniosgarbi.desafiomvc.service.EventService;

import java.util.List;

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
        List<Participant> participants = this.eventService.findAllParticipantsFromEvent(activity);

        mv.addObject("activity", activity);
        mv.addObject("eventId", eventId);

        mv.addObject("participants", this.eventService.findAllParticipantsFromEvent(activity));
        

        return mv;
    }  
    
    @PostMapping("/presence-list")
    public ModelAndView getPresenceList(Activity activity, @RequestParam Long eventId, BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView("redirect:/event/edit?isFromActivity=true&id=" + eventId);

        if(bindingResult.hasErrors()) {
            mv.addObject("message", "error");
            return mv;
        }

        try {
            this.activityService.save(activity);
            mv.addObject("message", "success");
        } catch(Exception e) {
            mv.addObject("error", e.getMessage());
        }
        return mv;
    }


}
