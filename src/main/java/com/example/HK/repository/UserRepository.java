package com.example.HK.repository;

import com.example.HK.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<Object> findByAccount(String account);

    @Query("SELECT u.id, u.account, u.name, u.branchId, u.departmentId, u.isStopped, b.name, d.name FROM User u " +
            "INNER JOIN Branch b ON u.branchId = b.id INNER JOIN Department d ON u.departmentId = d.id")
    public List<Object[]> findAllUser();

    @Modifying
    @Query("UPDATE User u SET u.isStopped = :isStopped, u.updatedDate = CURRENT_TIMESTAMP WHERE u.id = :id")
    public void saveStatus(@Param("isStopped")short isStopped, @Param("id")Integer id);

    public boolean existsByAccount(String account);

    public boolean existsByAccountAndIdNot(String account, int id);

    @Query("SELECT u.id, u.account, COUNT(m) FROM User u LEFT JOIN Message m ON u.id = m.userId GROUP BY u.id, u.account")
    public List<Object[]> findUserMessageCount();

    @Query("SELECT u.id, u.account, COUNT(c) FROM User u LEFT JOIN Comment c ON u.id = c.userId GROUP BY u.id, u.account")
    public List<Object[]> findUserCommentCount();
}
