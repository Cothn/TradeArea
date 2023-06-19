package com.example.tradeArea.service;

import com.example.tradeArea.entity.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OfferService {

    public Page<Offer> getAll(int pageNum, int pageSize, String sortBy, boolean ascending);

    public Offer getById(Long id);

    public void edit(Offer offer);

    public void add(Offer offer);

    public void delete(Offer offer);


}
