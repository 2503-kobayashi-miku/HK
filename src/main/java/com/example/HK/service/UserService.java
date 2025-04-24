package com.example.HK.service;

import com.example.HK.controller.form.UserForm;
import com.example.HK.dto.UserBranchDepartment;
import com.example.HK.dto.UserCommentCount;
import com.example.HK.dto.UserMessageCount;
import com.example.HK.repository.UserRepository;
import com.example.HK.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

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

    @Transactional
    public void saveIsStopped(Integer id, short isStopped) {
        userRepository.saveStatus(isStopped, id);
    }

    /*
     * アカウント重複チェック(登録)
     */
    public boolean existsUserByAccount(String account) {
        return userRepository.existsByAccount(account);
    }

    /*
     * アカウント重複チェック(編集)
     */
    public boolean existsUserByAccountAndIdNot(String account, int id) {
        return userRepository.existsByAccountAndIdNot(account, id);
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
        if(!reqUser.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(reqUser.getPassword()));
        } else {
            user.setPassword(editUser(reqUser.getId()).getPassword());
        }
        user.setName(reqUser.getName());
        user.setBranchId(reqUser.getBranchId());
        user.setDepartmentId(reqUser.getDepartmentId());
        user.setIsStopped(reqUser.getIsStopped());
        return user;
    }

    /*
     * レコード1件取得
     */
    public UserForm editUser(int id) {
        List<User> results = new ArrayList<>();
        results.add(userRepository.findById(id).orElse(null));
        if(results.get(0) != null){
            List<UserForm> users = setUserForm(results);
            return users.get(0);
        }else{
            return null;
        }
    }

    /*
     * DBから取得したデータをFormに設定
     */
    private List<UserForm> setUserForm(List<User> results) {
        List<UserForm> users = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            UserForm user = new UserForm();
            User result = results.get(i);
            user.setId(result.getId());
            user.setAccount(result.getAccount());
            user.setPassword(result.getPassword());
            user.setName(result.getName());
            user.setBranchId(result.getBranchId());
            user.setDepartmentId(result.getDepartmentId());
            user.setIsStopped(result.getIsStopped());
            user.setCreatedDate(result.getCreatedDate());
            user.setUpdatedDate(result.getUpdatedDate());
            users.add(user);
        }
        return users;
    }

    public List<UserMessageCount> findUserMessageCount() {
        List<Object[]> results = userRepository.findUserMessageCount();
        List<UserMessageCount> counts = setUserMessageCount(results);
        return counts;
    }

    private List<UserMessageCount> setUserMessageCount(List<Object[]> results) {
        List<UserMessageCount> counts = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            UserMessageCount count = new UserMessageCount();
            Object[] result = results.get(i);
            count.setId((int)result[0]);
            count.setAccount((String) result[1]);
            count.setCount((Long) result[2]);
            counts.add(count);
        }
        return counts;
    }

    public List<UserCommentCount> findUserCommentCount() {
        List<Object[]> results = userRepository.findUserCommentCount();
        List<UserCommentCount> counts = setUserCommentCount(results);
        return counts;
    }

    private List<UserCommentCount> setUserCommentCount(List<Object[]> results) {
        List<UserCommentCount> counts = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            UserCommentCount count = new UserCommentCount();
            Object[] result = results.get(i);
            count.setId((int)result[0]);
            count.setAccount((String) result[1]);
            count.setCount((Long) result[2]);
            counts.add(count);
        }
        return counts;
    }
}
