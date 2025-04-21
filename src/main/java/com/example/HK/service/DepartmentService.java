package com.example.HK.service;

import com.example.HK.controller.form.DepartmentForm;
import com.example.HK.repository.DepartmentRepository;
import com.example.HK.repository.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    DepartmentRepository departmentRepository;

    /*
     * レコード全件取得処理
     */
    public List<DepartmentForm> findAllDepartment() {
        List<Department> results = departmentRepository.findAll();
        List<DepartmentForm> departments = setDepartmentForm(results);
        return departments;
    }

    /*
     * DBから取得したデータをFormに設定
     */
    private List<DepartmentForm> setDepartmentForm(List<Department> results) {
        List<DepartmentForm> departments = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            DepartmentForm department = new DepartmentForm();
            Department result = results.get(i);
            department.setId(result.getId());
            department.setName(result.getName());
            department.setCreatedDate(result.getCreatedDate());
            department.setUpdatedDate(result.getUpdatedDate());
            departments.add(department);
        }
        return departments;
    }
}
