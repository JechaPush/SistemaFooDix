package com.example.SistemaDePromociones.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller principal para manejar las páginas estáticas del sistema
 */
@Controller
public class HomeController {
    
    /**
     * Página principal / inicio
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    /**
     * Página de login
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    /**
     * Página de selección de tipo de registro
     */
    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }
    
    /**
     * Página de recuperación de contraseña
     */
    @GetMapping("/recuperar-password")
    public String recuperarPassword() {
        return "recuperar-password";
    }

    
}
