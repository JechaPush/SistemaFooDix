package com.example.SistemaDePromociones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender emailSender;

    public void sendVerificationCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Código de verificación FoodIx");
        message.setText("Tu código de verificación es: " + code + "\n\n" +
                      "Este código expirará en 10 minutos.\n\n" +
                      "Si no solicitaste este código, por favor ignora este mensaje.");

        emailSender.send(message);
    }
}