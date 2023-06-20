package com.example.tradearea.service;

import com.example.tradearea.entity.Offer;
import org.springframework.data.domain.Page;


public interface OfferService {

    Page<Offer> getAll(int pageNum, int pageSize, String sortBy, boolean ascending);

    Offer getById(Long id);

    void edit(Offer offer);

    void add(Offer offer);

    void delete(Offer offer);


}
