package tech.antoniosgarbi.desafiomvc.controller;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tech.antoniosgarbi.desafiomvc.model.Activity;
import tech.antoniosgarbi.desafiomvc.model.Delivery;
import tech.antoniosgarbi.desafiomvc.model.Event;
import tech.antoniosgarbi.desafiomvc.model.Participant;
import tech.antoniosgarbi.desafiomvc.service.ActivityService;
import tech.antoniosgarbi.desafiomvc.service.EventService;

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
    public ModelAndView getPresenceList(@RequestParam Long id, @RequestParam Long eventId, @RequestParam(required = false) boolean newGroupFormation) {
        ModelAndView mv = new ModelAndView("/activity/activity_delivered.html");

        if(newGroupFormation) {
            
        }
        
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
            System.out.println("binding errors");
            return mv;
        }

        try {
            this.activityService.save(activity);
            mv.addObject("message", "success");
            System.out.println("try success");
        } catch(Exception e) {
            mv.addObject("error", e.getMessage());
            System.out.println("catch");
        }
        return mv;
    }


}
