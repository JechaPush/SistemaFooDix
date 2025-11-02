package com.example.SistemaDePromociones.controller;

import com.example.SistemaDePromociones.service.EmailService;
import com.example.SistemaDePromociones.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class VerificationController {

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/verificacion")
    public String showVerificationPage() {
        return "verificacion";
    }

    @PostMapping("/auth/send-code")
    @ResponseBody
    public String sendVerificationCode(@RequestParam String email, HttpSession session) {
        try {
            String code = verificationService.generateCode(email);
            emailService.sendVerificationCode(email, code);
            session.setAttribute("verificationEmail", email);
            return "{\"success\": true}";
        } catch (Exception e) {
            return "{\"success\": false, \"error\": \"" + e.getMessage() + "\"}";
        }
    }

    @PostMapping("/auth/verify-code")
    @ResponseBody
    public String verifyCode(@RequestParam String code, HttpSession session) {
        String email = (String) session.getAttribute("verificationEmail");
        if (email == null) {
            return "{\"success\": false, \"error\": \"Sesión expirada\"}";
        }

        boolean isValid = verificationService.verifyCode(email, code);
        if (isValid) {
            session.setAttribute("verifiedEmail", email);
            return "{\"success\": true, \"redirectUrl\": \"/registro\"}";
        } else {
            return "{\"success\": false, \"error\": \"Código inválido\"}";
        }
    }
}