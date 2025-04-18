package com.example.HK.repository;

import com.example.HK.repository.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("SELECT m.id, m.title, m.text, m.category, m.userId, u.account, u.name, u.branchId, u.departmentId," +
            " m.createdDate, m.updatedDate FROM Message m INNER JOIN User u ON m.userId = u.id WHERE m.createdDate" +
            " BETWEEN :start AND :end AND m.category LIKE %:category% ORDER BY m.createdDate DESC LIMIT :num")
    public List<Object[]> findByCreatedDateBetweenAndCategoryOrderByCreatedDateAsc(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("category") String category,
            @Param("num") int num);
    @Query("SELECT m.id, m.title, m.text, m.category, m.userId, u.account, u.name, u.branchId, u.departmentId," +
            " m.createdDate, m.updatedDate FROM Message m INNER JOIN User u ON m.userId = u.id  WHERE m.createdDate" +
            " BETWEEN :start AND :end ORDER BY m.createdDate DESC LIMIT :num")
    public List<Object[]> findByCreatedDateBetweenOrderByCreatedDateAsc(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("num") int num);
}
