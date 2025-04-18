package com.example.HK.repository;

import com.example.HK.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<Object> findByAccount(String account);

    @Query("SELECT u.id u.account u.name u.branchId u.departmentId u.isStopped b.name d.name FROM User u " +
            "INNER JOIN Branch b ON u.branchId = b.id INNER JOIN Department d ON u.departmentId = d.id")
    public List<Object[]> findAllUser();
}
