package com.example.HK.service;

import com.example.HK.dto.UserBranchDepartment;
import com.example.HK.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<UserBranchDepartment> findUserWithBranchWithDepartment() {
        List<Object[]> results = userRepository.findAllUser();
        List<UserBranchDepartment> users = setUserBranchDepartment(results);
        return users;
    }

    private List<UserBranchDepartment> setUserBranchDepartment(List<Object[]> results) {
        List<UserBranchDepartment> users = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            UserBranchDepartment user = new UserBranchDepartment();
            Object[] result = results.get(i);
            user.setId((int)result[0]);
            user.setAccount((String) result[1]);
            user.setName((String) result[2]);
            user.setBranchId((int) result[3]);
            user.setDepartmentId((int) result[4]);
            user.setIsStopped((short) result[5]);
            user.setBranchName((String) result[6]);
            user.setDepartmentName((String) result[7]);
            users.add(user);
        }
        return users;
    }
}
