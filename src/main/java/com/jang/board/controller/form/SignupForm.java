package com.jang.board.controller.form;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class SignupForm {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String userId;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식을 확인해주세요.")
    private String email;
    @NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @Pattern(regexp = "[0-9]{10,11}", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private String phone;
}
