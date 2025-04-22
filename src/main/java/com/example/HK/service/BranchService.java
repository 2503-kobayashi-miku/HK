package com.example.HK.service;

import com.example.HK.controller.form.BranchForm;
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
}
