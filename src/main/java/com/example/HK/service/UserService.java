package com.example.HK.service;

import com.example.HK.dto.UserBranchDepartment;
import com.example.HK.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        for (int i = 0; i < results.size(); i++) {
            UserBranchDepartment user = new UserBranchDepartment();
            Object[] result = results.get(i);

        }
    }
}
