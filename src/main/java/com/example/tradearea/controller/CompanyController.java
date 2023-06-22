package com.example.tradearea.controller;

import com.example.tradearea.entity.Company;
import com.example.tradearea.response.ResponsePage;
import com.example.tradearea.service.CompanyService;
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
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody Company company) {
        return companyService.add(company);
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
    @ResponseStatus(HttpStatus.OK)
    public Company read(@PathVariable(name = "id") long id) {
        final Company company = companyService.getById(id);
        return company;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody Company company) {
        companyService.edit(company);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable(name = "id") long id) {
        companyService.delete(id);
    }
}
