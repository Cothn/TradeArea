package com.example.tradeArea.service;

import com.example.tradeArea.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CompanyService {

    public Page<Company> getAll(int pageNum, int pageSize, String sortBy, boolean ascending);

    public Company getById(Long id);

    public void edit(Company company);

    public void add(Company company);

    public void delete(Company company);


}
