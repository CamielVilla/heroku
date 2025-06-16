package nl.thelastages.website.model.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateUserDto {
    @Nonnull
    @Email
    private String emailAddress;

    @Nonnull
    private String recaptchaToken;
}
