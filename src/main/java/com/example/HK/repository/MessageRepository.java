package com.example.HK.repository;

import com.example.HK.repository.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("SELECT m.id, m.title, m.text, m.category, m.userId, u.account, u.name, u.branchId, u.departmentId, m.createdDate, m.updatedDate FROM Message m INNER JOIN User u ON m.userId = u.id")
    public List<Object[]> findAllWithUser();
}
