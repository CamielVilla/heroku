package nl.thelastages.website.model.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Email;

public class CreateUserDto {

    @Nonnull
    @Email
    private String emailAddress;


    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
