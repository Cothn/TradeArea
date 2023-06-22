package com.example.tradearea.service;

import com.example.tradearea.entity.Company;
import com.example.tradearea.repository.CompanyRepository;
import com.example.tradearea.service.exceptions.DBException;
import com.example.tradearea.service.exceptions.OperationType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class CompanyServiceTests {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

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
    void add_addAbsolutelyNewCompany_companyAdded() {
        //Given
        long oldCount = companyRepository.count();

        //When
        long id = companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());


        //Then
        assertThat(companyRepository.findById(id)).isNotNull();
        assertThat(companyRepository.count()).isEqualTo(oldCount+1);
        
    }

    @Test
    void add_addAnExistCompany_notAddedAndThrowDBException () {
        //Given
        companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());
        long oldCount = companyRepository.count();

        //When
        DBException thrown = Assertions.assertThrows(DBException.class, () ->
                companyService.add(Company.builder()
                        .setName("newCompany")
                        .setUnp("000000000")
                        .setEmail("newCompany@mail.com")
                        .setDescription("newCompanyDescription")
                        .build()));

        //Then
        assertThat(companyRepository.count()).isEqualTo(oldCount);
        assertThat(thrown).isNotNull();
        assertThat(thrown.getOperationType()).isEqualTo(OperationType.CREATE);
        
    }

    @Test
    void add_addCompanyWithNullableAttributes_notAddedAndThrowDBException () {
        //Given
        long oldCount = companyRepository.count();

        //When
        DBException thrown = Assertions.assertThrows(DBException.class, () ->
                companyService.add(Company.builder()
                        .setName(null)
                        .setUnp(null)
                        .setEmail(null)
                        .setDescription(null)
                        .build()));

        //Then
        assertThat(companyRepository.count()).isEqualTo(oldCount);
        assertThat(thrown).isNotNull();
        assertThat(thrown.getOperationType()).isEqualTo(OperationType.CREATE);
    }

    @Test
    void add_addCompanyWithExistId_ThrowDBException() {
        //Given
        long id1 = companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());

        //When
        DBException thrown = Assertions.assertThrows(DBException.class, () ->
                companyService.add(Company.builder()
                        .setId(id1)
                        .setName("newCompany1")
                        .setUnp("000000001")
                        .setEmail("newCompany@mail.com1")
                        .setDescription("newCompanyDescription1")
                        .build()));


        //Then
        assertThat(thrown.getOperationType()).isEqualTo(OperationType.CREATE);
    }

    @Test
    void update_updateAllChangeableAttributes_allCompanyAttributesChanged() {
        //Given
        long id = companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());
        Company company = companyService.getById(id);


        //When
        companyService.edit(Company.builder()
                .setId(id)
                .setName("newCompanyUpdate")
                .setUnp("000000001")
                .setEmail("newCompanyUpdate@mail.com")
                .setCreated(company.getCreated())
                .setDescription("newCompanyDescription Update")
                .build());
        Company companyUpdated = companyService.getById(id);

        //Then
        assertThat(companyUpdated.getId()).isEqualTo(company.getId());
        assertThat(companyUpdated.getName()).isNotEqualTo(company.getName());
        assertThat(companyUpdated.getUnp()).isNotEqualTo(company.getUnp());
        assertThat(companyUpdated.getEmail()).isNotEqualTo(company.getEmail());
        assertThat(companyUpdated.getDescription()).isNotEqualTo(company.getDescription());
    }

    @Test
    void update_updateNotExistCompany_companiesListNotChangedAndThrowDBException() {
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
        DBException thrown = Assertions.assertThrows(DBException.class, () ->
                companyService.edit(Company.builder()
                        .setId(id)
                        .setName("newCompanyUpdate")
                        .setUnp("000000001")
                        .setEmail("newCompanyUpdate@mail.com")
                        .setCreated(company.getCreated())
                        .setDescription("newCompanyDescription Update")
                        .build()));

        //Then
        assertThat(companies).isEqualTo(companyRepository.findAll());
        assertThat(thrown).isNotNull();
        assertThat(thrown.getOperationType()).isEqualTo(OperationType.READE_ONE);
    }

    @Test
    void update_CompanyWithAlreadyExistUniqueParams_companiesListNotChangedAndThrowException() {
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
        Exception thrown = Assertions.assertThrows(RuntimeException.class, () ->
                companyService.edit(Company.builder()
                        .setId(id)
                        .setName("newCompany1")
                        .setUnp("000000001")
                        .setEmail("newCompany1@mail.com")
                        .setCreated(company.getCreated())
                        .setDescription("newCompanyDescription1")
                        .build()));

        //Then
        assertThat(companies).isEqualTo(companyRepository.findAll());
        assertThat(thrown).isNotNull();
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
        companyService.delete(id);

        //Then
        assertThat(companyRepository.findById(id).isEmpty()).isEqualTo(true);
        assertThat(companyRepository.count()).isEqualTo(oldCount-1);
    }

    @Test
    void delete_deleteNotExistCompany_countNotChangedAndThrowDBException() {
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
        DBException thrown = Assertions.assertThrows(DBException.class, () ->
                companyService.delete(id));

        //Then
        assertThat(companyRepository.count()).isEqualTo(oldCount);
        assertThat(thrown).isNotNull();
        assertThat(thrown.getOperationType()).isEqualTo(OperationType.READE_ONE);
    }


    @Test
    void getById_getCompany_companyFound() {
        //Given
        long id = companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());

        //When
        Company company = companyService.getById(id);

        //Then
        assertThat(company).isNotNull();
        assertThat(company.getName()).isEqualTo("newCompany");
    }

    @Test
    void getById_getNotExistedCompany_ThrowDBException() {
        //Given
        long id = companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());
        companyService.delete(id);

        //When
        DBException thrown = Assertions.assertThrows(DBException.class, () ->
                companyService.getById(id));

        //Then
        assertThat(thrown.getOperationType()).isEqualTo(OperationType.READE_ONE);
    }

    @Test
    void getAll_getPageOfCompanies_foundNotEmptyPageOfCompanies() {
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
        Page<Company> companies = companyService.
                getAll(0,3, "id", false);

        //Then
        assertThat(companies.isEmpty()).isEqualTo(false);
    }



    @Test
    void getAll_getWithPageSizeMoreThanCompaniesCont_listSizeEqualsCompaniesCount() {
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
        Page<Company> companies = companyService.
                getAll(0, 4, "id", false);


        //Then
        assertThat(companies.getNumberOfElements()).isEqualTo(companyRepository.count());
    }

    @Test
    void getAll_getWithPageMoreThanRealPageExist_ThrowDBException() {
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
        DBException thrown = Assertions.assertThrows(DBException.class, () ->
                companyService.getAll(1, 4, "id", false));

        //Then
        assertThat(thrown.getOperationType()).isEqualTo(OperationType.READE_PAGE);
    }
}
