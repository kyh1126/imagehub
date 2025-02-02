package com.example.imagehub.application.port.in;

public interface AuthUseCase {

    void signUp(SignUpCommand signUpCommand);

    String login(SignInCommand signInCommand);

}
