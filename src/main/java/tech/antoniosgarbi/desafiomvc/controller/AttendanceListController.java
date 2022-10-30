package tech.antoniosgarbi.desafiomvc.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import tech.antoniosgarbi.desafiomvc.model.AttendanceList;
import tech.antoniosgarbi.desafiomvc.model.Participant;
import tech.antoniosgarbi.desafiomvc.service.AttendanceListService;
import tech.antoniosgarbi.desafiomvc.service.EventService;

@Controller
@RequestMapping("/attendance-list")
@AllArgsConstructor
public class AttendanceListController {
    private final AttendanceListService attendanceListService;
    private final EventService eventService;

    @GetMapping
    public ModelAndView getForm(@RequestParam Long id, @RequestParam Long eventId) {
        ModelAndView mv = new ModelAndView("/attendance_list/attendance_list.html");
        
        mv.addObject("eventId", eventId);
        mv.addObject("attendanceList", this.attendanceListService.findById(id));
        mv.addObject("participants", this.eventService.getAllParticipantsFromEventId(eventId));
        
        return mv;
    }

    @PostMapping
    public ModelAndView postFormAttendanceList(@Valid AttendanceList attendanceList, @RequestParam Long eventId, BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView("/attendance_list/attendance_list.html");
        
        mv.addObject("participants", this.eventService.getAllParticipantsFromEventId(eventId));

        if(bindingResult.hasErrors()) {
            mv.addObject("message", "error");
            return mv;
        }

        try {
            AttendanceList saved = this.attendanceListService.save(attendanceList);
            List<Participant> participants = this.eventService.findAllParticipantsFromEvent(eventId);

            mv.addObject("eventId", eventId);
            mv.addObject("attendanceList", saved);
            mv.addObject("participants", participants);
            mv.addObject("message", "Lista atualizada com Sucesso!");
            mv.addObject("updated", "true");

        } catch(Exception e) {
            mv.addObject("error", e.getMessage());

        }
        return mv;
    
    }


}
