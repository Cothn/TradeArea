package com.example.tradeArea.service.implementation;



import com.example.tradeArea.entity.Company;
import com.example.tradeArea.entity.Offer;
import com.example.tradeArea.repository.OfferRepository;
import com.example.tradeArea.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        }
        else
        {
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
        offerRepository.save(offer);
    }

    @Override
    public void add(Offer offer) {
        offerRepository.save(offer);
    }

    @Override
    @Transactional
    public void delete(Offer offer) {
        offerRepository.delete(offer);
    }
}
