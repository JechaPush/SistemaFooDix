package com.example.SistemaDePromociones.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

@Service
public class VerificationService {
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public String generateCode(String email) {
        String code = String.format("%06d", random.nextInt(1000000));
        verificationCodes.put(email, code);
        return code;
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = verificationCodes.get(email);
        if (storedCode != null && storedCode.equals(code)) {
            verificationCodes.remove(email); // Remove the code once used
            return true;
        }
        return false;
    }

    public void removeCode(String email) {
        verificationCodes.remove(email);
    }
}