package com.example.HK.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserComment {

    private int id;
    private String text;
    private int userId;
    private int messageId;
    private String account;
    private String name;
    private int branchId;
    private int departmentId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
