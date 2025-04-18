package com.example.HK.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentForm {

    private int id;
    @NotBlank(message = "メッセージを入力してください")
    @Length(max= 500, message = "500文字以内で入力してください")
    private String text;
    private int userId;
    private int messageId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
