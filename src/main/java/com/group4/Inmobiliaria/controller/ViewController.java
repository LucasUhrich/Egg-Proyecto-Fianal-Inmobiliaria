package com.group4.Inmobiliaria.controller;

import com.group4.Inmobiliaria.entidades.Propiedad;
import com.group4.Inmobiliaria.entidades.Usuario;
import com.group4.Inmobiliaria.service.PropiedadService;
import com.group4.Inmobiliaria.service.UsuarioService;
import com.group4.Inmobiliaria.utils.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class ViewController {

    @Autowired
    PropiedadService propiedadService;
    
    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/")
    public String index(Model model) {
        List<Propiedad> propiedades = propiedadService.listarPropiedades();
        model.addAttribute("propiedades", propiedades);        
        
        Usuario logged = Session.getUserSession();
        
        System.out.println(logged);
        
        if (logged != null && logged.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin";
        }

        return "index";
    }

    @GetMapping("/nosotros")
    public String contacto(Model model) {

        return "nosotros";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        List<Usuario> usuarios = usuarioService.getAllUsers();
        
        model.addAttribute("usuarios", usuarios);
        
        return "administrador";
    }
}
