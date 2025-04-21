package com.example.HK.controller.form;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserForm {

    private int id;
    @NotBlank(message = "アカウントを入力してください")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "アカウントは半角英数字かつ6文字以上20文字以下で入力してください")
    private String account;
    @NotBlank(message = "パスワードを入力してください")
    @Pattern(regexp = "^[!-~]{6,20}$", message = "パスワードは半角文字かつ6文字以上20文字以下で入力してください")
    private String password;
    private String confirmPassword;
    @AssertTrue(message = "パスワードと確認用パスワードが一致しません")
    public boolean passwordConfirm() {
        if (password == null || confirmPassword == null) {
            return false;
        }
        return password.equals(confirmPassword);
    }
    @NotBlank(message = "氏名を入力してください")
    @Length(max = 10,  message = "氏名は10文字以下で入力してください")
    private String name;
    @NotNull(message = "支社を選択してください")
    private Integer branchId;
    @NotNull(message = "部署を選択してください")
    private Integer departmentId;
    private short isStopped;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
