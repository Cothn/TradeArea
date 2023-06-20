package com.example.tradearea.service.implementation;



import com.example.tradearea.entity.Company;
import com.example.tradearea.entity.Offer;
import com.example.tradearea.repository.CompanyRepository;
import com.example.tradearea.service.CompanyService;
import com.example.tradearea.service.exceptions.DBException;
import com.example.tradearea.service.exceptions.OperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

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
        } else {
            page = PageRequest.of(pageNum, pageSize, Sort.by(sortBy).descending());
        }
        try{
            Page<Company> company = companyRepository.findAll(page);
            if (!company.isEmpty()){
                return company;
            }
            throw new DBException(OperationType.READE_PAGE, page.toString());
        }catch (RuntimeException e){
            throw new DBException(OperationType.READE_PAGE, page.toString(), e);
        }
    }

    @Override
    @Transactional
    public Company getById(Long id) {
        try {
            Optional<Company> company = companyRepository.findById(id);
            if (company.isPresent()){
                return company.get();
            }
            throw new DBException(OperationType.READE_ONE, "id = "+id);
        }catch (RuntimeException e){
            throw new DBException(OperationType.READE_ONE, "id = "+id, e);
        }
    }

    @Override
    @Transactional
    public void edit(Company company) {
        try{
            companyRepository.save(company);
        }catch (RuntimeException e){
            throw new DBException(OperationType.UPDATE, company.toString(), e);
        }
    }

    @Override
    public void add(Company company) {
        try {
            company.setCreated(LocalDateTime.now());
            companyRepository.save(company);
        }catch (RuntimeException e){
            throw new DBException(OperationType.CREATE, company.toString(), e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try{
            companyRepository.delete(getById(id));
        } catch (RuntimeException e){
            throw new DBException(OperationType.DELETE, "id = "+id, e);
        }
    }
}
