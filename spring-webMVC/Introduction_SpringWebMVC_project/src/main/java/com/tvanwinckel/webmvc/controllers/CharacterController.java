package com.tvanwinckel.webmvc.controllers;

import com.tvanwinckel.webmvc.models.Character;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/characters")
public class CharacterController {

    private Character character;

    public CharacterController(Character character) {
        this.character = new Character("BigSwordGuy", "Warrior", "Horde", 55);
    }

    @GetMapping
    public String personForm(final Model model) {
        model.addAttribute("character", character);
        return "characterForm";
    }

    @PostMapping
    public String personSubmit(@ModelAttribute final Character character) {
        return "characterView";
    }
}
