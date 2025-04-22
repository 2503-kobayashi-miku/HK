package com.example.HK.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageForm {

    private int id;
    @NotBlank(message = "件名を入力してください")
    @Length(max = 30, message = "件名は30文字以内で入力してください")
    private String title;
    @NotBlank(message = "本文を入力してください")
    @Length(max = 1000, message = "本文は1000文字以内で入力してください")
    private String text;
    @NotBlank(message = "カテゴリを入力してください")
    @Length(max = 10, message = "カテゴリは10文字以内で入力してください")
    private String category;
    private int userId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
