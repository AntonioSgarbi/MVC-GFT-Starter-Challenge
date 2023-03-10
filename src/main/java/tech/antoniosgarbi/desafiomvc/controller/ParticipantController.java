package tech.antoniosgarbi.desafiomvc.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;
import tech.antoniosgarbi.desafiomvc.model.Participant;
import tech.antoniosgarbi.desafiomvc.service.ParticipantService;

import javax.validation.Valid;

@Controller
@RequestMapping("/participant")
@AllArgsConstructor
public class ParticipantController {
    private final ParticipantService participantService;

    @GetMapping("/edit")
    public ModelAndView edit(@RequestParam(required = false) Long id) {
        ModelAndView mv = new ModelAndView("participant/form.html");

        Participant participant;

        if(id==null) {
            participant = new Participant();
        }else {
            try {
                participant = participantService.findById(id);
            }catch(Exception e) {
                participant = new Participant();
                mv.addObject("message", e.getMessage());
            }
        }

        mv.addObject("participant", participant);

        return mv;

    }

    @PostMapping("/edit")
    public ModelAndView save(@Valid Participant participant, BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView("participant/form.html");

        boolean novo = participant.getId() == null;

        if(bindingResult.hasErrors()) {
            mv.addObject("participant", participant);
            return mv;
        }

        participantService.save(participant);

        if(novo) {
            mv.addObject("participant", new Participant());
        }else {
            mv.addObject("participant", participant);
        }

        mv.addObject("message", "Desenvolvedor salvo com sucesso");

        return mv;

    }

    @GetMapping
    public ModelAndView findAll(Pageable pageable) {
        ModelAndView mv = new ModelAndView("participant/list.html");

        mv.addObject("page", participantService.findAll(pageable));

        return mv;

    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("redirect:/participant");

        try {
            participantService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Desenvolvedor exclu??do com sucesso.");
        }catch(Exception e) {
            redirectAttributes.addFlashAttribute("message", "Erro ao excluir desenvolvedor!"+e.getMessage());
        }

        return mv;
    }

    @GetMapping("/name/{name}/")
    public ResponseEntity<Page<Participant>> findByName(@PathVariable String name, Pageable pageable) {
        return ResponseEntity.ok(this.participantService.findByName(name, pageable));
    }

}
