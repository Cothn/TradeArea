package com.example.tradearea.service.implementation;



import com.example.tradearea.entity.Offer;
import com.example.tradearea.repository.CompanyRepository;
import com.example.tradearea.repository.OfferRepository;
import com.example.tradearea.service.OfferService;
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
public class OfferServiceImpl implements OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    @Transactional
    public Page<Offer> getAll(int pageNum, int pageSize, String sortBy, boolean ascending) {
        Pageable page;
        if (ascending) {
            page = PageRequest.of(pageNum, pageSize, Sort.by(sortBy).ascending());
        } else {
            page = PageRequest.of(pageNum, pageSize, Sort.by(sortBy).descending());
        }
        return offerRepository.findAll(page);
    }
    
    @Override
    @Transactional
    public Offer getById(Long id) {
        return offerRepository.findById(id).get();
    }

    @Override
    @Transactional
    public void edit(Offer offer) {
        offer.setUpdated(LocalDateTime.now());
        offerRepository.save(offer);
    }

    @Override
    public void add(Offer offer) {
        offer.setUpdated(LocalDateTime.now());
        offerRepository.save(offer);
    }

    @Override
    @Transactional
    public void delete(Offer offer) {
        offerRepository.delete(offer);
    }
}
