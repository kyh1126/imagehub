package com.example.imagehub.adapter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "로그인 요청 정보")
@Getter
@NoArgsConstructor
public class SignInRequest {

    @Schema(description = "회원 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "회원 ID는 필수입니다.")
    private String userId;

    @Schema(description = "비밀번호", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

}
