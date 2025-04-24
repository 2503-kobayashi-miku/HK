package com.example.HK.service;

import com.example.HK.controller.form.BranchForm;
import com.example.HK.dto.BranchCommentCount;
import com.example.HK.dto.BranchMessageCount;
import com.example.HK.repository.BranchRepository;
import com.example.HK.repository.entity.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BranchService {
    @Autowired
    BranchRepository branchRepository;

    /*
     * レコード全件取得処理
     */
    public List<BranchForm> findAllBranch() {
        List<Branch> results = branchRepository.findAll();
        List<BranchForm> branches = setBranchForm(results);
        return branches;
    }

    /*
     * DBから取得したデータをFormに設定
     */
    private List<BranchForm> setBranchForm(List<Branch> results) {
        List<BranchForm> branches = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            BranchForm branch = new BranchForm();
            Branch result = results.get(i);
            branch.setId(result.getId());
            branch.setName(result.getName());
            branch.setCreatedDate(result.getCreatedDate());
            branch.setUpdatedDate(result.getUpdatedDate());
            branches.add(branch);
        }
        return branches;
    }

    public List<BranchMessageCount> findBranchMessageCount() {
        List<Object[]> results = branchRepository.findBranchMessageCount();
        List<BranchMessageCount> counts = setBranchMessageCount(results);
        return counts;
    }

    private List<BranchMessageCount> setBranchMessageCount(List<Object[]> results) {
        List<BranchMessageCount> counts = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            BranchMessageCount count = new BranchMessageCount();
            Object[] result = results.get(i);
            count.setId((int)result[0]);
            count.setName((String) result[1]);
            count.setCount((Long) result[2]);
            counts.add(count);
        }
        return counts;
    }

    public List<BranchCommentCount> findBranchCommentCount() {
        List<Object[]> results = branchRepository.findBranchCommentCount();
        List<BranchCommentCount> counts = setBranchCommentCount(results);
        return counts;
    }

    private List<BranchCommentCount> setBranchCommentCount(List<Object[]> results) {
        List<BranchCommentCount> counts = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            BranchCommentCount count = new BranchCommentCount();
            Object[] result = results.get(i);
            count.setId((int)result[0]);
            count.setName((String) result[1]);
            count.setCount((Long) result[2]);
            counts.add(count);
        }
        return counts;
    }
}
