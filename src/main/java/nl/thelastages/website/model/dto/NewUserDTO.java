package nl.thelastages.website.model.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Email;

public class NewUserDTO {

    @Nonnull
    private String name;

    @Nonnull
    @Email
    private String emailAddress;
    @Nonnull
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
