package com.example.HK.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserMessage {

    private int id;
    private String title;
    private String text;
    private String category;
    private int userId;
    private String account;
    private String name;
    private int branchId;
    private int departmentId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
