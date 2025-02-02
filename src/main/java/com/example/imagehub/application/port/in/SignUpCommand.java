package com.example.imagehub.application.port.in;

import com.example.imagehub.common.SelfValidating;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class SignUpCommand extends SelfValidating<SignUpCommand> {

    @NotBlank(message = "회원 ID는 필수입니다.")
    String userId;
    String username;
    @NotBlank(message = "비밀번호는 필수입니다.")
    String password;
    String role;

    public SignUpCommand(String userId, String username, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = StringUtils.isBlank(role) ? "USER" : role;
        this.validateSelf();
    }
}
