package tech.antoniosgarbi.desafiomvc.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tech.antoniosgarbi.desafiomvc.model.Activity;
import tech.antoniosgarbi.desafiomvc.model.Event;
import tech.antoniosgarbi.desafiomvc.model.Group;
import tech.antoniosgarbi.desafiomvc.model.Participant;
import tech.antoniosgarbi.desafiomvc.service.EventService;

import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/edit")
    public ModelAndView edit(@RequestParam(required = false) Long id) {
        ModelAndView mv = new ModelAndView("event/form.html");
        Event event;

        if(id==null) {
            event = eventService.createEmptyEvent();
        }else {
            try {
                event = eventService.findById(id);
            }catch(Exception e) {
                event = new Event();
                mv.addObject("message", e.getMessage());
            }
        }
        mv.addObject("event", event);
        return mv;
    }

    @PostMapping("/edit")
    public ModelAndView salvarDesenvolvedor(Event event, BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView("event/form.html");
        
        boolean isNewRegister = event.getId() == null;

        if(bindingResult.hasErrors()) {
            mv.addObject("message", "error");
            return mv;
        }
        try {
            event = eventService.save(event);

            if(isNewRegister) {
                mv.addObject("message", "Evento salvo com sucesso");
            }else {
                mv.addObject("message", "Evento atualizado com sucesso");
            }
            return mv;
        } catch(Exception e) {
            mv.addObject("message", e.getMessage());
        }
        mv.addObject("evnet", event);
        
        return mv;
        
    }

    @GetMapping
    public ModelAndView findAll() {
        ModelAndView mv = new ModelAndView("event/list.html");

        mv.addObject("list", eventService.findAll());

        return mv;

    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("redirect:/event");

        try {
            eventService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Evento excluído com sucesso.");
        }catch(Exception e) {
            redirectAttributes.addFlashAttribute("message", "Erro ao excluir evento!"+ e.getMessage());
        }

        return mv;
    }
}
