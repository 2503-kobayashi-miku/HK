package com.example.HK.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BranchMessageCount {

    private int id;
    private String name;
    private Long count;
}