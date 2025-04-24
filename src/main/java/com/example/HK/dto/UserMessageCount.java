package com.example.HK.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserMessageCount {

    private int id;
    private String account;
    private Long count;
}
