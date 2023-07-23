package dev.aj.controller;

import dev.aj.service.VetService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;

@RequiredArgsConstructor
public class VetController {

    private final VetService vetService;

    public String listVets(Model model) {
        model.addAttribute("vets", vetService.findAll());
        return "vets/index";
    }



}
