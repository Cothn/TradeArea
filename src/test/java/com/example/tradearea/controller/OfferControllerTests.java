package com.example.tradearea.controller;

import com.example.tradearea.entity.Company;
import com.example.tradearea.entity.Offer;
import com.example.tradearea.repository.CompanyRepository;
import com.example.tradearea.repository.OfferRepository;
import com.example.tradearea.service.CompanyService;
import com.example.tradearea.service.OfferService;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
public class OfferControllerTests {

    @Autowired
    private OfferService offerService;

    @Autowired
    private OfferRepository offerRepository;

    private final String REQUEST_URL
            = "http://localhost:8080/offers";

    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyRepository companyRepository;

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

        offerRepository.findAll().forEach(offer -> offerRepository.deleteById(offer.getId()));
        companyRepository.findAll().forEach(company -> companyRepository.deleteById(company.getId()));
    }


    @Test
    void add_addAbsolutelyNewOffer_offerCreated() {
        //Given
        long oldCount = offerRepository.count();
        long company_id = companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());

        //When
        HttpEntity<Offer> request = new HttpEntity<>(Offer.builder()
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription")
                .setPhone("+000-00-000-00-00 000")
                .setPrice(1)
                .setAmount(1)
                .build());
        ResponseEntity<String> response = restTemplate
                .exchange(REQUEST_URL, HttpMethod.POST, request, String.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(companyRepository.findById(Long.valueOf(response.getBody()))).isNotNull();
        assertThat(offerRepository.count()).isEqualTo(oldCount+1);
        
    }

    @Test
    void add_addAnExistOffer_HttpStatusBadRequest() {
        //Given
        long company_id = companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());
        offerService.add(Offer.builder()
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription")
                .setPhone("+000-00-000-00-00 000")
                .setPrice(1)
                .setAmount(1)
                .build());
        long oldCount = offerRepository.count();

        //When
        HttpEntity<Offer> request = new HttpEntity<>(Offer.builder()
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription")
                .setPhone("+000-00-000-00-00 000")
                .setPrice(1)
                .setAmount(1)
                .build());
        ResponseEntity<String> response = restTemplate
                .exchange(REQUEST_URL, HttpMethod.POST, request, String.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(offerRepository.count()).isEqualTo(oldCount);
    }

    @Test
    void add_addOfferWithNullableAttributes_HttpStatusBadRequest() {
        //Given
        long oldCount = offerRepository.count();

        //When
        HttpEntity<Offer> request = new HttpEntity<>(Offer.builder()
                .setCompany(null)
                .setDescription(null)
                .setPhone(null)
                .setPrice(null)
                .setAmount(null)
                .build());
        ResponseEntity<String> response = restTemplate
                .exchange(REQUEST_URL, HttpMethod.POST, request, String.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(offerRepository.count()).isEqualTo(oldCount);
    }

    @Test
    void add_addOfferWithExistId_HttpStatusBadRequest() {
        //Given
        long company_id = companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());
        long id1 = offerService.add(Offer.builder()
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription")
                .setPhone("+000-00-000-00-00 000")
                .setPrice(1)
                .setAmount(1)
                .build());

        //When
        HttpEntity<Offer> request = new HttpEntity<>(Offer.builder()
                .setId(id1)
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription")
                .setPhone("+000-00-000-00-00 001")
                .setPrice(1)
                .setAmount(1)
                .build());
        ResponseEntity<String> response = restTemplate
                .exchange(REQUEST_URL, HttpMethod.POST, request, String.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void add_addOfferWithNotExistCompanyId_HttpStatusBadRequest() {
        //Given
        long company_id = companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());
        companyService.delete(company_id);

        //When
        HttpEntity<Offer> request = new HttpEntity<>(Offer.builder()
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription")
                .setPhone("+000-00-000-00-00 001")
                .setPrice(1)
                .setAmount(1)
                .build());
        ResponseEntity<String> response = restTemplate
                .exchange(REQUEST_URL, HttpMethod.POST, request, String.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void update_updateAllChangeableAttributes_allOfferAttributesChangedAndHttpStatusOK() {
        //Given
        long company_id1 = companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());
        long company_id2 = companyService.add(Company.builder()
                .setName("newCompany1")
                .setUnp("000000001")
                .setEmail("newCompany1@mail.com")
                .setDescription("newCompanyDescription1")
                .build());
        long id = offerService.add(Offer.builder()
                .setCompany(Company.builder().setId(company_id1).build())
                .setDescription("newOfferDescription")
                .setPhone("+000-00-000-00-00 000")
                .setPrice(1)
                .setAmount(1)
                .build());
        Offer offer = offerService.getById(id);


        //When
        HttpEntity<Offer> request = new HttpEntity<>(Offer.builder()
                .setId(id)
                .setCompany(Company.builder().setId(company_id2).build())
                .setDescription("newOfferDescription Update")
                .setPhone("+000-00-000-00-00 111")
                .setPrice(100)
                .setAmount(10000)
                .build());
        ResponseEntity<String> response = restTemplate
                .exchange(REQUEST_URL, HttpMethod.PUT, request, String.class);

        Offer offerUpdated = offerService.getById(id);

        //Then
        assertThat(offerUpdated.getId()).isEqualTo(offer.getId());
        assertThat(offerUpdated.getCompany()).isNotEqualTo(offer.getCompany());
        assertThat(offerUpdated.getPhone()).isNotEqualTo(offer.getPhone());
        assertThat(offerUpdated.getPrice()).isNotEqualTo(offer.getPrice());
        assertThat(offerUpdated.getAmount()).isNotEqualTo(offer.getAmount());
        assertThat(offerUpdated.getUpdated()).isNotEqualTo(offer.getUpdated());
        assertThat(offerUpdated.getDescription()).isNotEqualTo(offer.getDescription());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void update_updateNotExistOffer_offersListNotChangedAndHttpStatusNotFound() {
        //Given
        long company_id = companyService.add(Company.builder()
                .setName("newCompany1")
                .setUnp("000000001")
                .setEmail("newCompany1@mail.com")
                .setDescription("newCompanyDescription1")
                .build());
        long id = offerService.add(Offer.builder()
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription")
                .setPhone("+000-00-000-00-00 000")
                .setPrice(1)
                .setAmount(1)
                .build());
        offerService.delete(id);
        Iterable<Offer> offers = offerRepository.findAll();

        //When
        HttpEntity<Offer> request = new HttpEntity<>(Offer.builder()
                .setId(id)
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription Update")
                .setPhone("+000-00-000-00-00 111")
                .setPrice(100)
                .setAmount(10000)
                .build());
        ResponseEntity<String> response = restTemplate
                .exchange(REQUEST_URL, HttpMethod.PUT, request, String.class);

        //Then
        assertThat(offers).isEqualTo(offerRepository.findAll());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void update_OfferWithAlreadyExistUniqueParams_offersListNotChangedAndHttpStatusBadRequest() {
        //Given
        long company_id = companyService.add(Company.builder()
                .setName("newCompany1")
                .setUnp("000000001")
                .setEmail("newCompany1@mail.com")
                .setDescription("newCompanyDescription1")
                .build());
        long id = offerService.add(Offer.builder()
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription")
                .setPhone("+000-00-000-00-00 000")
                .setPrice(1)
                .setAmount(1)
                .build());
        offerService.add(Offer.builder()
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription")
                .setPhone("+000-00-000-00-00 001")
                .setPrice(1)
                .setAmount(1)
                .build());
        Iterable<Offer> offers = offerRepository.findAll();

        //When
        HttpEntity<Offer> request = new HttpEntity<>(Offer.builder()
                .setId(id)
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription Update")
                .setPhone("+000-00-000-00-00 001")
                .setPrice(100)
                .setAmount(10000)
                .build());
        ResponseEntity<String> response = restTemplate
                .exchange(REQUEST_URL, HttpMethod.PUT, request, String.class);

        //Then
        assertThat(offers).isEqualTo(offerRepository.findAll());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void update_OfferWithNotExistCompanyId_offersListNotChangedAndHttpStatusBadRequest() {
        //Given
        long company_id = companyService.add(Company.builder()
                .setName("newCompany")
                .setUnp("000000000")
                .setEmail("newCompany@mail.com")
                .setDescription("newCompanyDescription")
                .build());
        long company_id2 = companyService.add(Company.builder()
                .setName("newCompany1")
                .setUnp("000000001")
                .setEmail("newCompany1@mail.com")
                .setDescription("newCompanyDescription1")
                .build());
        long id = offerService.add(Offer.builder()
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription")
                .setPhone("+000-00-000-00-00 000")
                .setPrice(1)
                .setAmount(1)
                .build());
        companyService.delete(company_id2);
        Iterable<Offer> offers = offerRepository.findAll();

        //When
        HttpEntity<Offer> request = new HttpEntity<>(Offer.builder()
                .setId(id)
                .setCompany(Company.builder().setId(company_id2).build())
                .setDescription("newOfferDescription Update")
                .setPhone("+000-00-000-00-00 111")
                .setPrice(100)
                .setAmount(10000)
                .build());
        ResponseEntity<String> response = restTemplate
                .exchange(REQUEST_URL, HttpMethod.PUT, request, String.class);

        //Then
        assertThat(offers).isEqualTo(offerRepository.findAll());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void delete_deleteExistOffer_offerDeleted() {
        //Given
        long company_id = companyService.add(Company.builder()
                .setName("newCompany1")
                .setUnp("000000001")
                .setEmail("newCompany1@mail.com")
                .setDescription("newCompanyDescription1")
                .build());
        long id = offerService.add(Offer.builder()
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription")
                .setPhone("+000-00-000-00-00 000")
                .setPrice(1)
                .setAmount(1)
                .build());
        long oldCount = offerRepository.count();

        //When
        ResponseEntity<String> response = restTemplate
                .exchange(
                        REQUEST_URL+"/"+id,
                        HttpMethod.DELETE,
                        new HttpEntity<String>(""),
                        String.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(offerRepository.findById(id).isEmpty()).isEqualTo(true);
        assertThat(offerRepository.count()).isEqualTo(oldCount-1);
    }

    @Test
    void delete_deleteNotExistOffer_countNotChangedAndHttpStatusNotFound() {
        //Given
        long company_id = companyService.add(Company.builder()
                .setName("newCompany1")
                .setUnp("000000001")
                .setEmail("newCompany1@mail.com")
                .setDescription("newCompanyDescription1")
                .build());
        long id = offerService.add(Offer.builder()
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription")
                .setPhone("+000-00-000-00-00 000")
                .setPrice(1)
                .setAmount(1)
                .build());
        offerService.delete(id);
        long oldCount = offerRepository.count();

        //When
        ResponseEntity<String> response = restTemplate
                .exchange(
                        REQUEST_URL+"/"+id,
                        HttpMethod.DELETE,
                        new HttpEntity<String>(""),
                        String.class);

        //Then
        assertThat(offerRepository.count()).isEqualTo(oldCount);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void read_readOffer_offerFound() {
        //Given
        long company_id = companyService.add(Company.builder()
                .setName("newCompany1")
                .setUnp("000000001")
                .setEmail("newCompany1@mail.com")
                .setDescription("newCompanyDescription1")
                .build());
        Offer offer = Offer.builder()
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription")
                .setPhone("+000-00-000-00-00 000")
                .setPrice(1)
                .setAmount(1)
                .build();
        long id = offerService.add(offer);

        //When
        ResponseEntity<Offer> response = restTemplate
                .exchange(
                        REQUEST_URL+"/"+id,
                        HttpMethod.GET,
                        new HttpEntity<String>(""),
                        Offer.class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(offer);
    }

    @Test
    void read_readNotExistedOffer_HttpStatusNotFound() {
        //Given
        long company_id = companyService.add(Company.builder()
                .setName("newCompany1")
                .setUnp("000000001")
                .setEmail("newCompany1@mail.com")
                .setDescription("newCompanyDescription1")
                .build());
        long id = offerService.add(Offer.builder()
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription")
                .setPhone("+000-00-000-00-00 000")
                .setPrice(1)
                .setAmount(1)
                .build());
        offerService.delete(id);

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
    void read_readPageOfOffers_HttpStatusOk() {
        //Given
        long company_id = companyService.add(Company.builder()
                .setName("newCompany1")
                .setUnp("000000001")
                .setEmail("newCompany1@mail.com")
                .setDescription("newCompanyDescription1")
                .build());
        offerService.add(Offer.builder()
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription")
                .setPhone("+000-00-000-00-00 000")
                .setPrice(1)
                .setAmount(1)
                .build());
        offerService.add(Offer.builder()
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription1")
                .setPhone("+000-00-000-00-00 001")
                .setPrice(1)
                .setAmount(1)
                .build());
        offerService.add(Offer.builder()
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription2")
                .setPhone("+000-00-000-00-00 002")
                .setPrice(1)
                .setAmount(1)
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
        long company_id = companyService.add(Company.builder()
                .setName("newCompany1")
                .setUnp("000000001")
                .setEmail("newCompany1@mail.com")
                .setDescription("newCompanyDescription1")
                .build());
        offerService.add(Offer.builder()
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription")
                .setPhone("+000-00-000-00-00 000")
                .setPrice(1)
                .setAmount(1)
                .build());
        offerService.add(Offer.builder()
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription1")
                .setPhone("+000-00-000-00-00 001")
                .setPrice(1)
                .setAmount(1)
                .build());
        offerService.add(Offer.builder()
                .setCompany(Company.builder().setId(company_id).build())
                .setDescription("newOfferDescription2")
                .setPhone("+000-00-000-00-00 002")
                .setPrice(1)
                .setAmount(1)
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
