package com.example.HK.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBranchDepartment {

    private int id;
    private String account;
    private String name;
    private int branchId;
    private int departmentId;
    private short isStopped;
    private String branchName;
    private String departmentName;
}
