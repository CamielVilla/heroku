package nl.thelastages.website.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import nl.thelastages.website.model.dto.CreateUserDto;
import nl.thelastages.website.model.dto.UserDto;
import nl.thelastages.website.model.entity.User;
import nl.thelastages.website.respository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final String MESSAGE = "<html><body><p>Dear board game fanatic,<br><br>" +
            "We want to thank you for your interest in The Last Ages...<br><br>" +
            "Kind Regards,<br>Camiel, Jasper, Ruben & Yuri<br>info@thelastages.com" +
            "</p></body></html>";

    public UserService(UserRepository userRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    public Boolean addEmail(CreateUserDto dto) {
        if (userRepository.findEmailByEmail(dto.getEmailAddress()).isPresent()) {
            return false; // Already subscribed
        }

        User user = new User();
        user.setEmail(dto.getEmailAddress());

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("info@thelastages.com");
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

    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setEmail(user.getEmail());
        dto.setId(user.getId());
        return dto;
    }

    public List<User> getAllEmails() {
        return userRepository.findAll();
    }
}

