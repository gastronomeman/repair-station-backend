package com.rs.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class MailUtils {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${project-config.email}")
    private String email;
    @Value("${spring.mail.username}")
    private String username;

    @Async
    public void sendEMail(String subject, String text)  {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(username); //邮件发送者
            mailMessage.setTo(email); // 邮件发给的人
            mailMessage.setSubject(subject);  // 邮件主题
            mailMessage.setText(text);  // 邮件内容

            javaMailSender.send(mailMessage);
            log.info("邮件发送成功");
        } catch (Exception e) {
            log.error("邮件发送失败 {}", e.getMessage());
        }
    }

}
