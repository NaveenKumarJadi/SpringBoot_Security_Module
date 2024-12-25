package com.naveen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.naveen.entity.UserInfo;
import com.naveen.repository.UserInfoRepository;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private UserInfoRepository userInfoRepository;

    public String generateOTP() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }

    public void sendOtpEmail(UserInfo user) {
        String otp = generateOTP();
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userInfoRepository.save(user);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Email Verification OTP");
        message.setText("Your OTP for email verification is: " + otp + 
                       "\nThis OTP will expire in 5 minutes.");

        mailSender.send(message);
    }

    public boolean verifyOtp(String email, String otp) {
        UserInfo user = userInfoRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp().equals(otp)) {
            LocalDateTime otpGeneratedTime = user.getOtpGeneratedTime();
            if (LocalDateTime.now().isBefore(otpGeneratedTime.plusMinutes(5))) {
                user.setEmailVerified(true);
                user.setOtp(null);
                user.setOtpGeneratedTime(null);
                userInfoRepository.save(user);
                return true;
            }
        }
        return false;
    }
}