package com.example.HK.controller.form;

import com.example.HK.validation.ValidationGroups.Edit;
import com.example.HK.validation.ValidationGroups.Signup;
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
    @NotBlank(message = "アカウントを入力してください", groups = {Signup.class, Edit.class})
    private String account;
    @NotBlank(message = "パスワードを入力してください", groups = Signup.class)
    private String password;
    private String confirmPassword;
    @NotBlank(message = "氏名を入力してください", groups = {Signup.class, Edit.class})
    @Length(max = 10,  message = "氏名は10文字以下で入力してください", groups = {Signup.class, Edit.class})
    private String name;
    @NotNull(message = "支社を選択してください", groups = {Signup.class, Edit.class})
    private Integer branchId;
    @NotNull(message = "部署を選択してください", groups = {Signup.class, Edit.class})
    private Integer departmentId;
    private short isStopped;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
