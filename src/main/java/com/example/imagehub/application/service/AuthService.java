package com.example.imagehub.application.service;

import com.example.imagehub.application.port.in.AuthUseCase;
import com.example.imagehub.application.port.in.SignInCommand;
import com.example.imagehub.application.port.in.SignUpCommand;
import com.example.imagehub.application.port.out.CreateUserPort;
import com.example.imagehub.application.port.out.TokenProviderPort;
import com.example.imagehub.common.UseCase;
import com.example.imagehub.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@UseCase
@Transactional(readOnly = true)
public class AuthService implements AuthUseCase {

    private final CreateUserPort createUserPort;
    private final TokenProviderPort tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    @Override
    public void signUp(SignUpCommand signUpCommand) {
        User user = User.of(signUpCommand, passwordEncoder);

        createUserPort.create(user);
    }

    @Override
    public String login(SignInCommand signInCommand) {
        var authentication = authenticate(signInCommand);

        return tokenProvider.generateToken(authentication);
    }

    private Authentication authenticate(SignInCommand signInCommand) {
        var unauthenticatedToken =
                UsernamePasswordAuthenticationToken.unauthenticated(signInCommand.getUserId(), signInCommand.getPassword());
        var authentication = authenticationManager.authenticate(unauthenticatedToken);

        if (!authentication.isAuthenticated()) {
            throw new RuntimeException("Invalid credentials");
        }

        return authentication;
    }
}
