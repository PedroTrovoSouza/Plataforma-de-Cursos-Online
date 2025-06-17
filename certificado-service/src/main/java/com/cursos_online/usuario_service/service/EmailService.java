package com.cursos_online.usuario_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmail(String para, String assunto, String corpo) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(corpo, false);
            helper.setFrom(new InternetAddress("seuemail@gmail.com", "Plataforma - Começou o Curso"));
            mailSender.send(message);
            System.out.println("[EmailService] E-mail enviado para: " + para);
        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}