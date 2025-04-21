package com.example.HK.service;

import com.example.HK.controller.form.UserForm;
import com.example.HK.dto.UserBranchDepartment;
import com.example.HK.repository.UserRepository;
import com.example.HK.repository.entity.User;
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

    /*
     * アカウント重複チェック(登録)
     */
    public boolean existsUserByAccount(String account) {
        return userRepository.existsByAccount(account);
    }

    /*
     * レコード追加
     */
    public void saveUser(UserForm reqUser) {
        User saveUser = setUserEntity(reqUser);
        userRepository.save(saveUser);
    }

    /*
     * リクエストから取得した情報をEntityに設定
     */
    private User setUserEntity(UserForm reqUser) {
        User user = new User();
        user.setId(reqUser.getId());
        user.setAccount(reqUser.getAccount());
        user.setPassword(reqUser.getPassword());
        user.setName(reqUser.getName());
        user.setBranchId(reqUser.getBranchId());
        user.setDepartmentId(reqUser.getDepartmentId());
        user.setIsStopped(reqUser.getIsStopped());
        return user;
    }
}
