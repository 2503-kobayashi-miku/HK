package com.example.HK.repository;

import com.example.HK.repository.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT c.id, c.text, c.userId, c.messageId, u.account, u.name, u.branchId, u.departmentId, c.createdDate, c.updatedDate FROM Comment c INNER JOIN User u ON c.userId = u.id")
    public List<Object[]> findAllWithUser();
}
