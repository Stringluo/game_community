package com.service.impl;

import com.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;

@Service
public class SendMailServiceImpl implements SendMailService {

    @Value("${spring.mail.username}")
    private String fromAddress;

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public SendMailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public Boolean sendMail(String toMail, String vCode, String type) {
        //创建邮件正文
        Context context = new Context();
        context.setVariable("verifyCode", Arrays.asList(vCode.split("")));
        if("login".equals(type)){
            type = "登录账户";
        } else if ("register".equals(type)) {
            type = "注册用户";
        } else if ("findPassword".equals(type)) {
            type = "找回密码";
        }
        context.setVariable("type", type);

        //将模块引擎内容解析成html字符串
        String emailContent = templateEngine.process("mail", context);
        MimeMessage message=mailSender.createMimeMessage();
        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom(fromAddress);
            helper.setTo(toMail);
            helper.setSubject("获取验证码");
            helper.setText(emailContent,true);
            mailSender.send(message);
            return true;
        }catch (MessagingException e) {
            return false;
        }
    }
}
