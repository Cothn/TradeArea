package com.example.tradearea.controller;

import com.example.tradearea.entity.Company;
import com.example.tradearea.response.ResponsePage;
import com.example.tradearea.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public HttpStatus create(@RequestBody Company company) {
        companyService.add(company);
        return HttpStatus.CREATED;
    }

    @GetMapping
    public ResponsePage<Company> read(
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = true, defaultValue = "created") String sortBy,
            @RequestParam(required = true, defaultValue = "false") Boolean ascending) {

        final Page<Company> companies = companyService.getAll(pageNum, pageSize, sortBy, ascending);

        return new ResponsePage<>(companies);
    }

    @GetMapping(value = "/{id}")
    public Company read(@PathVariable(name = "id") long id) {
        final Company company = companyService.getById(id);
        return company;
    }

    @PutMapping
    public HttpStatus update(@RequestBody Company company) {
        companyService.edit(company);
        return HttpStatus.OK;
    }

    @DeleteMapping(value = "/{id}")
    public HttpStatus delete(@PathVariable(name = "id") long id) {
        companyService.delete(id);
        return HttpStatus.OK;
    }
}
