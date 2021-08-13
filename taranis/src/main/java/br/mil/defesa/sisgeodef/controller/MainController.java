package br.mil.defesa.sisgeodef.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
    @GetMapping({"/", "/index", "/home"})
    public String index(Model model, HttpSession session, Principal principal ) {
		return "index";
    }	

    
}
