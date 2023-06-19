package com.example.tradeArea.controller;

import com.example.tradeArea.entity.Company;
import com.example.tradeArea.response.ResponsePage;
import com.example.tradeArea.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


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
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = true, defaultValue = "created") String sortBy,
            @RequestParam(required = true, defaultValue = "false") Boolean ascending) {

        final Page<Company> companies = companyService.getAll(pageNum-1, pageSize, sortBy, ascending);

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
        companyService.delete(companyService.getById(id));
        return HttpStatus.OK;
    }
}
