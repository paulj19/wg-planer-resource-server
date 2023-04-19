package com.wgplaner.registration;

import com.wgplaner.common.validation.ValidEmail;
import com.wgplaner.common.validation.ValidPassword;
import com.wgplaner.common.validation.ValidUsername;
import com.wgplaner.core.AuthServer;
import javax.validation.constraints.NotNull;

public record RegistrationDto(
        @ValidUsername String username,
        @ValidEmail String email,
        @ValidPassword String password,
        Long oid,
        @NotNull
        AuthServer authServer) {
}
