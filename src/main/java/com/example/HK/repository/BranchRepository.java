package com.example.HK.repository;

import com.example.HK.repository.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {
    @Query("SELECT b.id, b.name, COUNT(m) FROM Branch b LEFT JOIN User u ON b.id = u.branchId LEFT JOIN Message m ON u.id = m.userId GROUP BY b.id, b.name")
    public List<Object[]> findBranchMessageCount();
    @Query("SELECT b.id, b.name, COUNT(c) FROM Branch b LEFT JOIN User u ON b.id = u.branchId LEFT JOIN Comment c ON u.id = c.userId GROUP BY b.id, b.name")
    public List<Object[]> findBranchCommentCount();
}
