package com.example.tradearea.service;

import com.example.tradearea.entity.Company;
import com.example.tradearea.entity.Offer;
import com.example.tradearea.repository.CompanyRepository;
import com.example.tradearea.repository.OfferRepository;
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
public class OfferServiceTests {

    @Autowired
    private OfferService offerService;

    @Autowired
    private OfferRepository offerRepository;

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

        offerRepository.findAll().forEach(offer -> offerRepository.deleteById(offer.getId()));
        companyRepository.findAll().forEach(company -> companyRepository.deleteById(company.getId()));
    }


    @Test
    void add_addAbsolutelyNewOffer_offerAdded() {
        //Given
        long oldCount = offerRepository.count();
        long company_id = companyService.add(Company.builder()
                .name("newCompany")
                .unp("000000000")
                .email("newCompany@mail.com")
                .description("newCompanyDescription")
                .build());

        //When
        long id = offerService.add(Offer.builder()
                .company(Company.builder().id(company_id).build())
                .description("newOfferDescription")
                .phone("+000-00-000-00-00 000")
                .price(1)
                .amount(1)
                .build());

        //Then
        assertThat(offerRepository.findById(id)).isNotNull();
        assertThat(offerRepository.count()).isEqualTo(oldCount+1);
        
    }

    @Test
    void add_addAnExistOffer_notAddedAndThrowDBException () {
        //Given
        long company_id = companyService.add(Company.builder()
                .name("newCompany")
                .unp("000000000")
                .email("newCompany@mail.com")
                .description("newCompanyDescription")
                .build());
        offerService.add(Offer.builder()
                .company(Company.builder().id(company_id).build())
                .description("newOfferDescription")
                .phone("+000-00-000-00-00 000")
                .price(1)
                .amount(1)
                .build());
        long oldCount = offerRepository.count();

        //When
        DBException thrown = Assertions.assertThrows(DBException.class, () ->
                offerService.add(Offer.builder()
                        .company(Company.builder().id(company_id).build())
                        .description("newOfferDescription")
                        .phone("+000-00-000-00-00 000")
                        .price(1)
                        .amount(1)
                        .build()));

        //Then
        assertThat(offerRepository.count()).isEqualTo(oldCount);
        assertThat(thrown).isNotNull();
        assertThat(thrown.getOperationType()).isEqualTo(OperationType.CREATE);
        
    }

    @Test
    void add_addOfferWithNullableAttributes_notAddedAndThrowDBException () {
        //Given
        long oldCount = offerRepository.count();

        //When
        DBException thrown = Assertions.assertThrows(DBException.class, () ->
                offerService.add(Offer.builder()
                        .company(null)
                        .description(null)
                        .phone(null)
                        .price(null)
                        .amount(null)
                        .build()));

        //Then
        assertThat(offerRepository.count()).isEqualTo(oldCount);
        assertThat(thrown).isNotNull();
        assertThat(thrown.getOperationType()).isEqualTo(OperationType.CREATE);
    }

    @Test
    void add_addOfferWithExistId_ThrowDBException() {
        //Given
        long company_id = companyService.add(Company.builder()
                .name("newCompany")
                .unp("000000000")
                .email("newCompany@mail.com")
                .description("newCompanyDescription")
                .build());
        long id1 = offerService.add(Offer.builder()
                .company(Company.builder().id(company_id).build())
                .description("newOfferDescription")
                .phone("+000-00-000-00-00 000")
                .price(1)
                .amount(1)
                .build());

        //When
        DBException thrown = Assertions.assertThrows(DBException.class, () ->
                offerService.add(Offer.builder()
                        .id(id1)
                        .company(Company.builder().id(company_id).build())
                        .description("newOfferDescription")
                        .phone("+000-00-000-00-00 001")
                        .price(1)
                        .amount(1)
                        .build()));


        //Then
        assertThat(thrown.getOperationType()).isEqualTo(OperationType.CREATE);
    }

    @Test
    void add_addOfferWithNotExistCompanyId_ThrowException() {
        //Given
        long company_id = companyService.add(Company.builder()
                .name("newCompany")
                .unp("000000000")
                .email("newCompany@mail.com")
                .description("newCompanyDescription")
                .build());
        companyService.delete(company_id);
        Iterable<Offer> offers = offerRepository.findAll();

        //When
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () ->
                offerService.add(Offer.builder()
                        .company(Company.builder().id(company_id).build())
                        .description("newOfferDescription")
                        .phone("+000-00-000-00-00 000")
                        .price(1)
                        .amount(1)
                        .build()));

        //Then
        assertThat(offers).isEqualTo(offerRepository.findAll());
        assertThat(thrown).isNotNull();
    }

    @Test
    void update_updateAllChangeableAttributes_allOfferAttributesChanged() {
        //Given
        long company_id1 = companyService.add(Company.builder()
                .name("newCompany")
                .unp("000000000")
                .email("newCompany@mail.com")
                .description("newCompanyDescription")
                .build());
        long company_id2 = companyService.add(Company.builder()
                .name("newCompany1")
                .unp("000000001")
                .email("newCompany1@mail.com")
                .description("newCompanyDescription1")
                .build());
        long id = offerService.add(Offer.builder()
                .company(Company.builder().id(company_id1).build())
                .description("newOfferDescription")
                .phone("+000-00-000-00-00 000")
                .price(1)
                .amount(1)
                .build());
        Offer offer = offerService.getById(id);


        //When
        offerService.edit(Offer.builder()
                .id(id)
                .company(Company.builder().id(company_id2).build())
                .description("newOfferDescription Update")
                .phone("+000-00-000-00-00 111")
                .price(100)
                .amount(10000)
                .build());
        Offer offerUpdated = offerService.getById(id);

        //Then
        assertThat(offerUpdated.getId()).isEqualTo(offer.getId());
        assertThat(offerUpdated.getCompany()).isNotEqualTo(offer.getCompany());
        assertThat(offerUpdated.getPhone()).isNotEqualTo(offer.getPhone());
        assertThat(offerUpdated.getPrice()).isNotEqualTo(offer.getPrice());
        assertThat(offerUpdated.getAmount()).isNotEqualTo(offer.getAmount());
        assertThat(offerUpdated.getUpdated()).isNotEqualTo(offer.getUpdated());
        assertThat(offerUpdated.getDescription()).isNotEqualTo(offer.getDescription());
    }

    @Test
    void update_OfferWithAlreadyExistUniqueParams_offersListNotChangedAndThrowException() {
        //Given
        long company_id = companyService.add(Company.builder()
                .name("newCompany1")
                .unp("000000001")
                .email("newCompany1@mail.com")
                .description("newCompanyDescription1")
                .build());
        long id = offerService.add(Offer.builder()
                .company(Company.builder().id(company_id).build())
                .description("newOfferDescription")
                .phone("+000-00-000-00-00 000")
                .price(1)
                .amount(1)
                .build());
        offerService.add(Offer.builder()
                .company(Company.builder().id(company_id).build())
                .description("newOfferDescription")
                .phone("+000-00-000-00-00 001")
                .price(1)
                .amount(1)
                .build());
        Iterable<Offer> offers = offerRepository.findAll();

        //When
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () ->
                offerService.edit(Offer.builder()
                        .id(id)
                        .company(Company.builder().id(company_id).build())
                        .description("newOfferDescription Update")
                        .phone("+000-00-000-00-00 001")
                        .price(100)
                        .amount(10000)
                        .build()));

        //Then
        assertThat(offers).isEqualTo(offerRepository.findAll());
        assertThat(thrown).isNotNull();
    }


    @Test
    void update_updateNotExistOffer_offersListNotChangedAndThrowDBException() {
        //Given
        long company_id = companyService.add(Company.builder()
                .name("newCompany1")
                .unp("000000001")
                .email("newCompany1@mail.com")
                .description("newCompanyDescription1")
                .build());
        long id = offerService.add(Offer.builder()
                .company(Company.builder().id(company_id).build())
                .description("newOfferDescription")
                .phone("+000-00-000-00-00 000")
                .price(1)
                .amount(1)
                .build());
        offerService.delete(id);
        Iterable<Offer> offers = offerRepository.findAll();

        //When
        DBException thrown = Assertions.assertThrows(DBException.class, () ->
                offerService.edit(Offer.builder()
                        .id(id)
                        .company(Company.builder().id(company_id).build())
                        .description("newOfferDescription Update")
                        .phone("+000-00-000-00-00 111")
                        .price(100)
                        .amount(10000)
                        .build()));

        //Then
        assertThat(offers).isEqualTo(offerRepository.findAll());
        assertThat(thrown).isNotNull();
        assertThat(thrown.getOperationType()).isEqualTo(OperationType.READE_ONE);
    }

    @Test
    void update_OfferWithNotExistCompanyId_offersListNotChangedAndThrowDBException() {
        //Given
        long company_id = companyService.add(Company.builder()
                .name("newCompany")
                .unp("000000000")
                .email("newCompany@mail.com")
                .description("newCompanyDescription")
                .build());
        long company_id2 = companyService.add(Company.builder()
                .name("newCompany1")
                .unp("000000001")
                .email("newCompany1@mail.com")
                .description("newCompanyDescription1")
                .build());
        long id = offerService.add(Offer.builder()
                .company(Company.builder().id(company_id).build())
                .description("newOfferDescription")
                .phone("+000-00-000-00-00 000")
                .price(1)
                .amount(1)
                .build());
        companyService.delete(company_id2);
        Iterable<Offer> offers = offerRepository.findAll();

        //When
        DBException thrown = Assertions.assertThrows(DBException.class, () ->
                offerService.edit(Offer.builder()
                        .id(id)
                        .company(Company.builder().id(company_id2).build())
                        .description("newOfferDescription Update")
                        .phone("+000-00-000-00-00 111")
                        .price(100)
                        .amount(10000)
                        .build()));

        //Then
        assertThat(offers).isEqualTo(offerRepository.findAll());
        assertThat(thrown).isNotNull();
        assertThat(thrown.getOperationType()).isEqualTo(OperationType.UPDATE);
    }

    @Test
    void delete_deleteExistOffer_offerDeleted() {
        //Given
        long company_id = companyService.add(Company.builder()
                .name("newCompany1")
                .unp("000000001")
                .email("newCompany1@mail.com")
                .description("newCompanyDescription1")
                .build());
        long id = offerService.add(Offer.builder()
                .company(Company.builder().id(company_id).build())
                .description("newOfferDescription")
                .phone("+000-00-000-00-00 000")
                .price(1)
                .amount(1)
                .build());
        long oldCount = offerRepository.count();

        //When
        offerService.delete(id);

        //Then
        assertThat(offerRepository.findById(id).isEmpty()).isEqualTo(true);
        assertThat(offerRepository.count()).isEqualTo(oldCount-1);
    }

    @Test
    void delete_deleteNotExistOffer_countNotChangedAndThrowDBException() {
        //Given
        long company_id = companyService.add(Company.builder()
                .name("newCompany1")
                .unp("000000001")
                .email("newCompany1@mail.com")
                .description("newCompanyDescription1")
                .build());
        long id = offerService.add(Offer.builder()
                .company(Company.builder().id(company_id).build())
                .description("newOfferDescription")
                .phone("+000-00-000-00-00 000")
                .price(1)
                .amount(1)
                .build());
        offerService.delete(id);
        long oldCount = offerRepository.count();

        //When
        DBException thrown = Assertions.assertThrows(DBException.class, () ->
                offerService.delete(id));

        //Then
        assertThat(offerRepository.count()).isEqualTo(oldCount);
        assertThat(thrown).isNotNull();
        assertThat(thrown.getOperationType()).isEqualTo(OperationType.READE_ONE);
    }


    @Test
    void getById_getOffer_offerFound() {
        //Given
        long company_id = companyService.add(Company.builder()
                .name("newCompany1")
                .unp("000000001")
                .email("newCompany1@mail.com")
                .description("newCompanyDescription1")
                .build());
        long id = offerService.add(Offer.builder()
                .company(Company.builder().id(company_id).build())
                .description("newOfferDescription")
                .phone("+000-00-000-00-00 000")
                .price(1)
                .amount(1)
                .build());

        //When
        Offer offer = offerService.getById(id);

        //Then
        assertThat(offer).isNotNull();
        assertThat(offer.getId()).isEqualTo(id);
    }

    @Test
    void getById_getNotExistedOffer_ThrowDBException() {
        //Given
        long company_id = companyService.add(Company.builder()
                .name("newCompany1")
                .unp("000000001")
                .email("newCompany1@mail.com")
                .description("newCompanyDescription1")
                .build());
        long id = offerService.add(Offer.builder()
                .company(Company.builder().id(company_id).build())
                .description("newOfferDescription")
                .phone("+000-00-000-00-00 000")
                .price(1)
                .amount(1)
                .build());
        offerService.delete(id);

        //When
        DBException thrown = Assertions.assertThrows(DBException.class, () ->
                offerService.getById(id));

        //Then
        assertThat(thrown.getOperationType()).isEqualTo(OperationType.READE_ONE);
    }

    @Test
    void getAll_getPageOfOffers_foundNotEmptyPageOfOffers() {
        //Given
        long company_id = companyService.add(Company.builder()
                .name("newCompany1")
                .unp("000000001")
                .email("newCompany1@mail.com")
                .description("newCompanyDescription1")
                .build());
        offerService.add(Offer.builder()
                .company(Company.builder().id(company_id).build())
                .description("newOfferDescription")
                .phone("+000-00-000-00-00 000")
                .price(1)
                .amount(1)
                .build());
        offerService.add(Offer.builder()
                .company(Company.builder().id(company_id).build())
                .description("newOfferDescription1")
                .phone("+000-00-000-00-00 001")
                .price(1)
                .amount(1)
                .build());
        offerService.add(Offer.builder()
                .company(Company.builder().id(company_id).build())
                .description("newOfferDescription2")
                .phone("+000-00-000-00-00 002")
                .price(1)
                .amount(1)
                .build());


        //When
        Page<Offer> offers = offerService.
                getAll(0,3, "id", false);

        //Then
        assertThat(offers.isEmpty()).isEqualTo(false);
    }



    @Test
    void getAll_getWithPageSizeMoreThanOffersCont_listSizeEqualsOffersCount() {
        //Given
        long company_id = companyService.add(Company.builder()
                .name("newCompany1")
                .unp("000000001")
                .email("newCompany1@mail.com")
                .description("newCompanyDescription1")
                .build());
        offerService.add(Offer.builder()
                .company(Company.builder().id(company_id).build())
                .description("newOfferDescription")
                .phone("+000-00-000-00-00 000")
                .price(1)
                .amount(1)
                .build());
        offerService.add(Offer.builder()
                .company(Company.builder().id(company_id).build())
                .description("newOfferDescription1")
                .phone("+000-00-000-00-00 001")
                .price(1)
                .amount(1)
                .build());
        offerService.add(Offer.builder()
                .company(Company.builder().id(company_id).build())
                .description("newOfferDescription2")
                .phone("+000-00-000-00-00 002")
                .price(1)
                .amount(1)
                .build());

        //When
        Page<Offer> offers = offerService.
                getAll(0, 4, "id", false);


        //Then
        assertThat(offers.getNumberOfElements()).isEqualTo(offerRepository.count());
    }

    @Test
    void getAll_getWithPageMoreThanRealPageExist_ThrowDBException() {
        //Given
        long company_id = companyService.add(Company.builder()
                .name("newCompany1")
                .unp("000000001")
                .email("newCompany1@mail.com")
                .description("newCompanyDescription1")
                .build());
        offerService.add(Offer.builder()
                .company(Company.builder().id(company_id).build())
                .description("newOfferDescription")
                .phone("+000-00-000-00-00 000")
                .price(1)
                .amount(1)
                .build());
        offerService.add(Offer.builder()
                .company(Company.builder().id(company_id).build())
                .description("newOfferDescription1")
                .phone("+000-00-000-00-00 001")
                .price(1)
                .amount(1)
                .build());
        offerService.add(Offer.builder()
                .company(Company.builder().id(company_id).build())
                .description("newOfferDescription2")
                .phone("+000-00-000-00-00 002")
                .price(1)
                .amount(1)
                .build());

        //When
        DBException thrown = Assertions.assertThrows(DBException.class, () ->
                offerService.getAll(1, 4, "id", false));

        //Then
        assertThat(thrown.getOperationType()).isEqualTo(OperationType.READE_PAGE);
    }
}
