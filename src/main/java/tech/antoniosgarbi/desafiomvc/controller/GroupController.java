package tech.antoniosgarbi.desafiomvc.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;
import tech.antoniosgarbi.desafiomvc.model.Group;
import tech.antoniosgarbi.desafiomvc.service.GroupService;

import javax.validation.Valid;

@RestController
@RequestMapping("/group")
@AllArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @GetMapping("/edit")
    public ModelAndView edit(@RequestParam(required = false) Long id) {
        ModelAndView mv = new ModelAndView("group/form.html");
        Group group;

        if(id==null) {
            group = new Group();
        }else {
            try {
                group = groupService.findById(id);
            }catch(Exception e) {
                group = new Group();
                mv.addObject("message", e.getMessage());
            }
        }

        mv.addObject("group", group);

        return mv;

    }

    @PostMapping("/edit")
    public ModelAndView salvarDesenvolvedor(@Valid Group group, BindingResult bindingResult) {
        System.out.println(group);
        ModelAndView mv = new ModelAndView("redirect:/group");

        boolean novo = group.getId() == null;

        if(bindingResult.hasErrors()) {
            mv.addObject("group", group);
            return mv;
        }

        groupService.save(group);

        mv.addObject("group", new Group());
        if(novo) {
            mv.addObject("message", "Grupo salvo com sucesso");
        }else {
            mv.addObject("message", "Grupo atualizado com sucesso");
        }

        return mv;

    }

    @GetMapping
    public ModelAndView findAll() {
        ModelAndView mv = new ModelAndView("group/list.html");

        mv.addObject("list", groupService.findAll());

        return mv;

    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("redirect:/group");

        try {
            groupService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Grupo excluído com sucesso.");
        }catch(Exception e) {
            redirectAttributes.addFlashAttribute("message", "Erro ao excluir grupo!"+ e.getMessage());
        }

        return mv;
    }
}
