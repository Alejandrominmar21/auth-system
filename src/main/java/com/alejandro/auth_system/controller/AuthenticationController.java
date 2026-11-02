package com.alejandro.auth_system.controller;

import com.alejandro.auth_system.entity.User;
import com.alejandro.auth_system.service.CustomUserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("register")
public class AuthenticationController {

    private final CustomUserDetailsService userDetailsService;

    public AuthenticationController(CustomUserDetailsService userDetailsService){
        this.userDetailsService=userDetailsService;
    }

    @GetMapping
    public String register(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "register";
    }

    @PostMapping
    public String postUser(@ModelAttribute("user") User user,
                           Model model,
                           RedirectAttributes redirectAttributes){

        userDetailsService.registerUser(user);
        redirectAttributes.addFlashAttribute("message","Confirma tu dirección email");
        return "redirect:/register";
    }
}
