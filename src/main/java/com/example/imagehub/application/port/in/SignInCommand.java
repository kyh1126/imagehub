package com.example.imagehub.application.port.in;

import com.example.imagehub.common.SelfValidating;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class SignInCommand extends SelfValidating<SignInCommand> {

    @NotBlank(message = "회원 ID는 필수입니다.")
    String userId;
    @NotBlank(message = "비밀번호는 필수입니다.")
    String password;

    public SignInCommand(String userId, String password) {
        this.userId = userId;
        this.password = password;
        this.validateSelf();
    }
}
