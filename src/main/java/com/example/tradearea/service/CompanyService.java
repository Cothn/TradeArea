package com.example.tradearea.service;

import com.example.tradearea.entity.Company;
import org.springframework.data.domain.Page;


public interface CompanyService {

    Page<Company> getAll(int pageNum, int pageSize, String sortBy, boolean ascending);

    Company getById(Long id);

    void edit(Company company);

    void add(Company company);

    void delete(Company company);


}
