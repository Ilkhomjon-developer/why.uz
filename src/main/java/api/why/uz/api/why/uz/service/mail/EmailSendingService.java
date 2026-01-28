package api.why.uz.api.why.uz.service.mail;


import api.why.uz.api.why.uz.enums.AppLanguage;
import api.why.uz.api.why.uz.enums.SmsType;
import api.why.uz.api.why.uz.util.JwtUtil;
import api.why.uz.api.why.uz.util.RandomNumberGenerator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class EmailSendingService {

    private final JavaMailSender javaMailSender;
    private final SmsHistoryService smsHistoryService;

    @Value("${spring.mail.username}")
    private String fromAccount;

    @Value("${server.domain}")
    private String serverDomain;

    public void sendRegistrationEmail(String email, Integer profileId, AppLanguage lang){
        String body = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Registration Confirmation</title>
                </head>
                <body style="margin:0; padding:0; font-family:'Arial', sans-serif; background-color:#f0f2f5;">
                <table width="100%%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td align="center">
                            <!-- Main container -->
                            <table width="600" cellpadding="0" cellspacing="0" border="0" style="background: linear-gradient(135deg, #6a11cb, #2575fc); padding:40px; border-radius:20px; box-shadow:0 15px 50px rgba(0,0,0,0.3); color:#fff;">
                
                                <!-- Header -->
                                <tr>
                                    <td align="center">
                                        <h1 style="font-size:36px; margin-bottom:15px; font-weight:bold;">Welcome to Our Service!</h1>
                                        <p style="font-size:18px; line-height:1.6; max-width:480px; margin:0 auto;">
                                            Thank you for registering! To activate your account, please confirm your email by clicking the button below.
                                        </p>
                                    </td>
                                </tr>
                
                                <!-- Button -->
                                <tr>
                                    <td align="center" style="padding:35px 0;">
                                        <a href="%s/auth/registration/email-verification/%s?lang=%s" style="
                                                background: linear-gradient(90deg, #ff6b6b, #f06595);
                                                color:#ffffff;
                                                text-decoration:none;
                                                padding:20px 50px;
                                                font-size:18px;
                                                font-weight:bold;
                                                border-radius:50px;
                                                display:inline-block;
                                                box-shadow:0 10px 35px rgba(0,0,0,0.35);
                                                transition: all 0.3s ease;
                                            ">
                                            Confirm Registration
                                        </a>
                                    </td>
                                </tr>
                
                            </table>
                        </td>
                    </tr>
                </table>
                </body>
                </html>
                """;

        body = String.format(body,serverDomain, JwtUtil.encode(profileId), lang.name());
        sendEmail(email, "Registration confirmation", body);
    }

    public void sendUpdateEmailCode(String email, int code, AppLanguage lang, SmsType smsType){


        smsHistoryService.save(email, code, "Registration confirmation code", smsType);

        // String message = "Your verification code is: ";
        String body = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Registration Confirmation</title>
                </head>
                <body style="margin:0; padding:0; font-family:'Arial', sans-serif; background-color:#f0f2f5;">
                <table width="100%%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td align="center">
                            <!-- Main container -->
                            <table width="600" cellpadding="0" cellspacing="0" border="0" style="background: linear-gradient(135deg, #6a11cb, #2575fc); padding:40px; border-radius:20px; box-shadow:0 15px 50px rgba(0,0,0,0.3); color:#fff;">
                
                                <!-- Header -->
                                <tr>
                                    <td align="center">
                                        <h1 style="font-size:36px; margin-bottom:15px; font-weight:bold;">Welcome to Our Service!</h1>
                                        <p style="font-size:18px; line-height:1.6; max-width:480px; margin:0 auto;">
                                            Thank you for registering! Use the verification code below to activate your account.
                                        </p>
                                    </td>
                                </tr>
                
                                <!-- Verification Code -->
                                <tr>
                                    <td align="center" style="padding:35px 0;">
                                        <div style="
                                                background-color: rgba(255,255,255,0.2);
                                                color: #ffffff;
                                                font-size: 32px;
                                                font-weight: bold;
                                                padding: 20px 40px;
                                                border-radius: 12px;
                                                letter-spacing: 4px;
                                                display: inline-block;
                                            ">%d</div>
                                    </td>
                                </tr>
                
                            </table>
                        </td>
                    </tr>
                </table>
                </body>
                </html>""";

        body = String.format(body,code, lang.name());
        sendEmail(email, "Registration confirmation", body);
    }


    protected void sendEmail(String email, String subject, String body){
        MimeMessage msg = javaMailSender.createMimeMessage();
        try {
            msg.setFrom(fromAccount);
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body, true);
            CompletableFuture.runAsync(() -> javaMailSender.send(msg)); // email sent through thread
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }


    public void sendResetPasswordEmail(String email, AppLanguage lang) {

        int code = RandomNumberGenerator.generate();
        String body = "Reset password code " + code;
        sendEmail(email, "Reset password code", body);
    }
//         Simple mail sender
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setFrom(fromAccount);
//        msg.setTo(email);
//        msg.setSubject(subject);
//        msg.setText(body);
//        javaMailSender.send(msg);

}
