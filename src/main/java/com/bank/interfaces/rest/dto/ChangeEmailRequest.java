package com.bank.interfaces.rest.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record ChangeEmailRequest(
    @Email @NotBlank @Size(min = 10, max = 250) String newEmail
) {
}
