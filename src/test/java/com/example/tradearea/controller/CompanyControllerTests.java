package com.example.tradearea.controller;

import com.example.tradearea.entity.Company;
import com.example.tradearea.repository.CompanyRepository;
import com.example.tradearea.service.CompanyService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CompanyControllerTests {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    private final String REQUEST_URL
            = "http://localhost:8080/companies";

    @Autowired
    private TestRestTemplate restTemplate;
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeAll
    static void setUpBeforeAll()  {
        postgres.start();

    }

    @AfterAll
    static void tearDownAfterAll()  {
        postgres.stop();
    }

    @AfterEach
    void tearDownAfterEach()  {

        companyRepository.findAll().forEach(company -> companyRepository.deleteById(company.getId()));

    }


    @Test
    void add_addAbsolutelyNewCompany_companyCreated() {
        //Given
        long oldCount = companyRepository.count();

        //When
        HttpEntity<Company> request = new HttpEntity<>(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());
        ResponseEntity<String> response = restTemplate
                .exchange(REQUEST_URL, HttpMethod.POST, request, String.class);


        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(companyRepository.findById(Long.valueOf(response.getBody()))).isNotNull();
        assertThat(companyRepository.count()).isEqualTo(oldCount+1);
    }

    @Test
    void add_addAnExistCompany_HttpStatusBadRequest() {
        //Given
        companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());
        long oldCount = companyRepository.count();

        //When
        HttpEntity<Company> request = new HttpEntity<>(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());
        ResponseEntity<String> response = restTemplate
                .exchange(REQUEST_URL, HttpMethod.POST, request, String.class);

        //Then
        assertThat(companyRepository.count()).isEqualTo(oldCount);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void add_addCompanyWithNullableAttributes_HttpStatusBadRequest() {
        //Given
        long oldCount = companyRepository.count();

        //When
        HttpEntity<Company> request = new HttpEntity<>(Company.builder()
                .setName(null)
                .setUnp(null)
                .setEmail(null)
                .setDescription(null)
                .build());
        ResponseEntity<String> response = restTemplate
                .exchange(REQUEST_URL, HttpMethod.POST, request, String.class);

        //Then
        assertThat(companyRepository.count()).isEqualTo(oldCount);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void add_addCompanyWithExistId_HttpStatusBadRequest() {
        //Given
        long id1 = companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());

        //When
        HttpEntity<Company> request = new HttpEntity<>(Company.builder()
                .setId(id1)
                .setName("newCompany1")
                .setUnp("000000001")
                .setEmail("newCompany@mail.com1")
                .setDescription("newCompanyDescription1")
                .build());
        ResponseEntity<String> response = restTemplate
                .exchange(REQUEST_URL, HttpMethod.POST, request, String.class);


        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void update_updateAllChangeableAttributes_allCompanyAttributesChangedAndHttpStatusOK() {
        //Given
        long id = companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());
        Company company = companyService.getById(id);


        //When
        HttpEntity<Company> request = new HttpEntity<>(Company.builder()
                .setId(id)
                .setName("newCompanyUpdate")
                .setUnp("000000001")
                .setEmail("newCompanyUpdate@mail.com")
                .setCreated(company.getCreated())
                .setDescription("newCompanyDescription Update")
                .build());
        ResponseEntity<String> response = restTemplate
                .exchange(REQUEST_URL, HttpMethod.PUT, request, String.class);
        Company companyUpdated = companyService.getById(id);

        //Then
        assertThat(companyUpdated.getId()).isEqualTo(company.getId());
        assertThat(companyUpdated.getName()).isNotEqualTo(company.getName());
        assertThat(companyUpdated.getUnp()).isNotEqualTo(company.getUnp());
        assertThat(companyUpdated.getEmail()).isNotEqualTo(company.getEmail());
        assertThat(companyUpdated.getDescription()).isNotEqualTo(company.getDescription());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void update_updateNotExistCompany_companiesListNotChangedAndHttpStatusNotFound() {
        //Given
        long id = companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());
        Company company = companyService.getById(id);
        companyService.delete(id);
        Iterable<Company> companies = companyRepository.findAll();

        //When
        HttpEntity<Company> request = new HttpEntity<>(Company.builder()
                .setId(id)
                .setName("newCompanyUpdate")
                .setUnp("000000001")
                .setEmail("newCompanyUpdate@mail.com")
                .setCreated(company.getCreated())
                .setDescription("newCompanyDescription Update")
                .build());
        ResponseEntity<String> response = restTemplate
                .exchange(REQUEST_URL, HttpMethod.PUT, request, String.class);


        //Then
        assertThat(companies).isEqualTo(companyRepository.findAll());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void update_CompanyWithAlreadyExistUniqueParams_companiesListNotChangedAndHttpStatusBadRequest() {
        //Given
        long id = companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());
        companyService.add(Company.builder()
                .setName("newCompany1")
                .setUnp("000000001")
                .setEmail("newCompany1@mail.com")
                .setDescription("newCompanyDescription1")
                .build());
        Iterable<Company> companies = companyRepository.findAll();
        Company company = companyService.getById(id);

        //When
        HttpEntity<Company> request = new HttpEntity<>(Company.builder()
                .setId(id)
                .setName("newCompany1")
                .setUnp("000000001")
                .setEmail("newCompany1@mail.com")
                .setCreated(company.getCreated())
                .setDescription("newCompanyDescription1")
                .build());
        ResponseEntity<String> response = restTemplate
                .exchange(REQUEST_URL, HttpMethod.PUT, request, String.class);

        //Then
        assertThat(companies).isEqualTo(companyRepository.findAll());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void delete_deleteExistCompany_companyDeleted() {
        //Given
        long id = companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());
        long oldCount = companyRepository.count();

        //When
        ResponseEntity<String> response = restTemplate
                .exchange(
                        REQUEST_URL+"/"+id,
                        HttpMethod.DELETE,
                        new HttpEntity<String>(""),
                        String.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(companyRepository.findById(id).isEmpty()).isEqualTo(true);
        assertThat(companyRepository.count()).isEqualTo(oldCount-1);
    }

    @Test
    void delete_deleteNotExistCompany_countNotChangedAndHttpStatusNotFound() {
        //Given
        long id = companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());
        companyService.delete(id);
        long oldCount = companyRepository.count();

        //When
        ResponseEntity<String> response = restTemplate
                .exchange(
                        REQUEST_URL+"/"+id,
                        HttpMethod.DELETE,
                        new HttpEntity<String>(""),
                        String.class);

        //Then
        assertThat(companyRepository.count()).isEqualTo(oldCount);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void read_readCompany_companyFound() {
        //Given
        Company company = Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build();
        long id = companyService.add(company);


        //When
        ResponseEntity<Company> response = restTemplate
                .exchange(
                        REQUEST_URL+"/"+id,
                        HttpMethod.GET,
                        new HttpEntity<String>(""),
                        Company.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(company);
    }

    @Test
    void read_readNotExistedCompany_HttpStatusNotFound() {
        //Given
        long id = companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());
        companyService.delete(id);

        //When
        ResponseEntity<String> response = restTemplate
                .exchange(
                        REQUEST_URL+"/"+id,
                        HttpMethod.GET,
                        new HttpEntity<String>(""),
                        String.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void read_readPageOfCompanies_HttpStatusOk() {
        //Given
        companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());
        companyService.add(Company.builder()
                .setName("newCompany1")
                .setUnp("000000001")
                .setEmail("newCompany1@mail.com")
                .setDescription("newCompanyDescription1")
                .build());
        companyService.add(Company.builder()
                .setName("newCompany2")
                .setUnp("000000002")
                .setEmail("newCompany2@mail.com")
                .setDescription("newCompanyDescription2")
                .build());


        //When
        ResponseEntity<String> response = restTemplate
                .exchange(
                        REQUEST_URL,
                        HttpMethod.GET,
                        new HttpEntity<String>(""),
                        String.class);

        //Then
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void read_readWithPageMoreThanRealPageExist_HttpStatusNotFound() {
        //Given
        companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());
        companyService.add(Company.builder()
                .setName("newCompany1")
                .setUnp("000000001")
                .setEmail("newCompany1@mail.com")
                .setDescription("newCompanyDescription1")
                .build());
        companyService.add(Company.builder()
                .setName("newCompany2")
                .setUnp("000000002")
                .setEmail("newCompany2@mail.com")
                .setDescription("newCompanyDescription2")
                .build());

        //When
        ResponseEntity<String> response = restTemplate
                .exchange(
                        REQUEST_URL+"?pageNum=1"+"&pageSize="+4,
                        HttpMethod.GET,
                        new HttpEntity<String>(""),
                        String.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
