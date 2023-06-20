package com.example.tradearea.service.implementation;



import com.example.tradearea.entity.Offer;
import com.example.tradearea.repository.OfferRepository;
import com.example.tradearea.service.OfferService;
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
public class OfferServiceImpl implements OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Override
    @Transactional
    public Page<Offer> getAll(int pageNum, int pageSize, String sortBy, boolean ascending) {

        Pageable page;
        if (ascending) {
            page = PageRequest.of(pageNum, pageSize, Sort.by(sortBy).ascending());
        } else {
            page = PageRequest.of(pageNum, pageSize, Sort.by(sortBy).descending());
        }
        try{
            Page<Offer> offer = offerRepository.findAll(page);
            if (!offer.isEmpty()){
                return offer;
            }
            throw new DBException(OperationType.READE_PAGE, page.toString());
        }catch (RuntimeException e){
            throw new DBException(OperationType.READE_PAGE, page.toString(), e);
        }
    }

    @Override
    @Transactional
    public Offer getById(Long id) {
        try {
            Optional<Offer> offer = offerRepository.findById(id);
            if (offer.isPresent()){
                return offer.get();
            }
            throw new DBException(OperationType.READE_ONE, "id = "+id);
        }catch (RuntimeException e){
            throw new DBException(OperationType.READE_ONE, "id = "+id, e);
        }
    }

    @Override
    @Transactional
    public void edit(Offer offer) {
        try{
            offer.setUpdated(LocalDateTime.now());
            offerRepository.save(offer);
        }catch (RuntimeException e){
            throw new DBException(OperationType.UPDATE, offer.toString(), e);
        }
    }

    @Override
    public void add(Offer offer) {
        try {
            offer.setUpdated(LocalDateTime.now());
            offerRepository.save(offer);
        }catch (RuntimeException e){
            throw new DBException(OperationType.CREATE, offer.toString(), e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try{
            offerRepository.delete(getById(id));
        } catch (RuntimeException e){
            throw new DBException(OperationType.DELETE, "id = "+id, e);
        }
    }
}
