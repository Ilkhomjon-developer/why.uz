package api.why.uz.api.why.uz.service.mail;


import api.why.uz.api.why.uz.enums.AppLanguage;
import api.why.uz.api.why.uz.util.JwtUtil;
import api.why.uz.api.why.uz.util.RandomNumberGenerator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class EmailSendingService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromAccount;

    @Value("${server.domain}")
    private String serverDomain;

    public void sendRegistrationEmail(String email, Integer profileId, AppLanguage lang){


        String body = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Registration Confirmation</title>\n" +
                "</head>\n" +
                "<body style=\"margin:0; padding:0; font-family:'Arial', sans-serif; background-color:#f0f2f5;\">\n" +
                "<table width=\"100%%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tr>\n" +
                "        <td align=\"center\">\n" +
                "            <!-- Main container -->\n" +
                "            <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background: linear-gradient(135deg, #6a11cb, #2575fc); padding:40px; border-radius:20px; box-shadow:0 15px 50px rgba(0,0,0,0.3); color:#fff;\">\n" +
                "\n" +
                "                <!-- Header -->\n" +
                "                <tr>\n" +
                "                    <td align=\"center\">\n" +
                "                        <h1 style=\"font-size:36px; margin-bottom:15px; font-weight:bold;\">Welcome to Our Service!</h1>\n" +
                "                        <p style=\"font-size:18px; line-height:1.6; max-width:480px; margin:0 auto;\">\n" +
                "                            Thank you for registering! To activate your account, please confirm your email by clicking the button below.\n" +
                "                        </p>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "\n" +
                "                <!-- Button -->\n" +
                "                <tr>\n" +
                "                    <td align=\"center\" style=\"padding:35px 0;\">\n" +
                "                        <a href=\"%s/auth/registration/email-verification/%s?lang=%s\" style=\"\n" +
                "                                background: linear-gradient(90deg, #ff6b6b, #f06595);\n" +
                "                                color:#ffffff;\n" +
                "                                text-decoration:none;\n" +
                "                                padding:20px 50px;\n" +
                "                                font-size:18px;\n" +
                "                                font-weight:bold;\n" +
                "                                border-radius:50px;\n" +
                "                                display:inline-block;\n" +
                "                                box-shadow:0 10px 35px rgba(0,0,0,0.35);\n" +
                "                                transition: all 0.3s ease;\n" +
                "                            \">\n" +
                "                            Confirm Registration\n" +
                "                        </a>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "\n" +

                "            </table>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>\n";

        body = String.format(body,serverDomain, JwtUtil.encode(profileId), lang.name());


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
