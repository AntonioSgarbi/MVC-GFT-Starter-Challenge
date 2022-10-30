package tech.antoniosgarbi.desafiomvc.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;
import tech.antoniosgarbi.desafiomvc.model.Event;
import tech.antoniosgarbi.desafiomvc.model.Group;
import tech.antoniosgarbi.desafiomvc.service.EventService;

import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

import javax.validation.Valid;

@RestController
@RequestMapping("/event")
@AllArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/edit")
    public ModelAndView edit(@RequestParam(required = false) Long id, @RequestParam(required = false) String message) {
        ModelAndView mv = new ModelAndView("event/form.html");
        Event event;
        event = eventService.createEmptyEvent();

        if (id != null) {
            try {
                event = eventService.findById(id);
                Collections.sort(event.getGroups(),
                        Comparator.comparingInt(Group::getScore).reversed().thenComparing(Group::getName));

            } catch (Exception e) {
                mv.addObject("error", e.getMessage());
                mv.addObject("event", event);
                mv.addObject("attendanceList", new TreeSet<>(event.getPresences()));
                return mv;
            }
        }

        if (message != null) {
            mv.addObject("message", message);
        }

        mv.addObject("event", event);
        mv.addObject("attendanceList", new TreeSet<>(event.getPresences()));

        return mv;
    }

    @PostMapping("/edit")
    public ModelAndView postForm(@Valid Event event, BindingResult bindingResult) {
        boolean isNewRegister = event.getId() == null;
        ModelAndView mv = new ModelAndView("/event/form.html");

        if (bindingResult.hasErrors()) {
            mv.addObject("event", new Event());
            mv.addObject("message", "error");
            return mv;
        }

        try {
            Event saved = eventService.save(event);

            if (isNewRegister) {
                mv.addObject("message", "Evento salvo com sucesso");
            } else {
                mv.addObject("message", "Evento atualizado com sucesso");
            }
            mv.addObject("event", saved);
            mv.addObject("attendanceList", new TreeSet<>(saved.getPresences()));

        } catch (Exception e) {
            if (!isNewRegister)
                mv = new ModelAndView("redirect:/event/edit?id=" + event.getId());
            else
                mv.addObject("event", event);

            mv.addObject("error", e.getMessage());
        }

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
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao excluir evento!" + e.getMessage());
        }

        return mv;
    }

}
