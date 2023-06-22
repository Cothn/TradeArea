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
            Page<Company> companies = companyRepository.findAll(page);
            if (!companies.getContent().isEmpty()){
                return companies;
            }
            throw new DBException(OperationType.READE_PAGE, page.toString());
        }catch (DBException e) {
            throw e;
        }
        catch (RuntimeException e){
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
        }catch (DBException e) {
            throw e;
        }
        catch (RuntimeException e){
            throw new DBException(OperationType.READE_ONE, "id = "+id, e);
        }
    }

    @Override
    @Transactional
    public void edit(Company company) {
        try{
            if (this.getById(company.getId()) != null)
                companyRepository.save(company);
        }catch (DBException e) {
            throw e;
        }catch (RuntimeException e) {
            throw new DBException(OperationType.UPDATE, company.toString(), e);
        }
    }

    @Override
    @Transactional
    public long add(Company company) {
        try {
            if (company.getId() != null)
                throw new DBException(OperationType.CREATE, company.toString());
            company.setCreated(LocalDateTime.now());
            return companyRepository.save(company).getId();
        }catch (DBException e) {
            throw e;
        }catch (RuntimeException e){
            throw new DBException(OperationType.CREATE, company.toString(), e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try{
            companyRepository.delete(getById(id));
        }catch (DBException e) {
            throw e;
        }catch (RuntimeException e){
            throw new DBException(OperationType.DELETE, "id = "+id, e);
        }
    }
}
