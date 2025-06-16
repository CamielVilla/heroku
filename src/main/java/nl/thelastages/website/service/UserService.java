package nl.thelastages.website.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import nl.thelastages.website.model.dto.CreateUserDto;
import nl.thelastages.website.model.entity.User;
import nl.thelastages.website.respository.UserRepository;
import nl.thelastages.website.security.RecaptchaValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService implements IUserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final RecaptchaValidator recaptchaValidator;

    @Value("${spring.mail.username}")
    private String userName;

    public UserService(UserRepository userRepository, JavaMailSender mailSender, RecaptchaValidator recaptchaValidator) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.recaptchaValidator = recaptchaValidator;
    }

    public Boolean addEmail(CreateUserDto dto) {
        if (!isRequestValid(dto)) {
            return false;
        }
        if (userRepository.findEmailByEmail(dto.getEmailAddress()).isPresent()) {
            return false; // Already subscribed
        }

        User user = new User();
        user.setEmail(dto.getEmailAddress());

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(userName);
            helper.setTo(dto.getEmailAddress());
            helper.setSubject("Thank you for your interest in The Last Ages");
            helper.setText(MESSAGE, true);

            mailSender.send(message);
            userRepository.save(user);
            return true;

        } catch (MessagingException e) {
            logger.error("Failed to send welcome email to {}", dto.getEmailAddress(), e);
            return false;
        }
    }
    private boolean isRequestValid(CreateUserDto dto) {
        if (!recaptchaValidator.isValid(dto.getRecaptchaToken())) {
            logger.warn("Blocked contact attempt due to failed reCAPTCHA");
            return false;
        }
        return true;
    }
    private static final String MESSAGE = "<html><body><p>Dear board game fanatic,<br><br>" +
            "We want to thank you for your interest in The Last Ages. We will contact you about future developments.<br><br>" +
            "Kind Regards,<br>Camiel, Jasper, Ruben & Yuri<br>info@thelastages.com" +
            "</p></body></html>";

}

