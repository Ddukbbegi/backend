package com.ddukbbegi.api.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record SignupRequestDto(
        @NotBlank(message = "이메일은 필수 입력값입니다.") @Email(message = "유효한 이메일 주소를 입력하세요.") @Size(max = 50) String email,
        @NotBlank(message = "비밀번호는 필수 입력값입니다.") @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
                message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8자 이상이어야 합니다."
        ) String password,
        @NotBlank(message = "이름은 필수 입력값입니다.") @Size(max = 10, message = "이름은 10자 이내로 입력해주세요.") String name,
        @NotBlank(message = "전화번호는 필수 입력값입니다.") @Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.") String phone,
        @NotBlank(message = "권한은 필수값 입니다.") String role) {

}
