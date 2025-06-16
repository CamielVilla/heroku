package nl.thelastages.website.security;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class RecaptchaValidator {

    private static final Logger log = LoggerFactory.getLogger(RecaptchaValidator.class);

    @Value("${recaptcha.secret}")
    private String secret;

    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        this.restTemplate = new RestTemplate();
    }

    public boolean isValid(String token) {
        String verifyUrl = "https://www.google.com/recaptcha/api/siteverify";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "secret=" + secret + "&response=" + token;
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(verifyUrl, HttpMethod.POST, request, Map.class);
            Map<?, ?> responseBody = response.getBody();

            boolean success = Boolean.TRUE.equals(responseBody.get("success"));
            double score = responseBody.get("score") instanceof Number ? ((Number) responseBody.get("score")).doubleValue() : 0.0;

            log.info("reCAPTCHA success={}, score={}", success, score);

            return success && score > 0.5;
        } catch (Exception e) {
            log.error("Failed to validate reCAPTCHA", e);
            return false;
        }
    }
}
