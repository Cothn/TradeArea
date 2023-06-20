package com.example.tradeArea.service.implementation;



import com.example.tradeArea.entity.Company;
import com.example.tradeArea.repository.CompanyRepository;
import com.example.tradeArea.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    @Transactional
    public Page<Company> getAll(int pageNum, int pageSize, String sortBy, boolean ascending) {
        Pageable page;
        if (ascending) {
            page = PageRequest.of(pageNum, pageSize, Sort.by(sortBy).ascending());
        }
        else
        {
            page = PageRequest.of(pageNum, pageSize, Sort.by(sortBy).descending());
        }
        return companyRepository.findAll(page);
    }

    @Override
    @Transactional
    public Company getById(Long id) {
        return companyRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void edit(Company company) {
        companyRepository.save(company);
    }

    @Override
    public void add(Company company) {
        company.setCreated(LocalDateTime.now());
        companyRepository.save(company);
    }

    @Override
    @Transactional
    public void delete(Company company) {
        companyRepository.delete(company);
    }
}
