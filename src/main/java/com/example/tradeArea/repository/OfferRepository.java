package com.example.tradeArea.repository;

import com.example.tradeArea.entity.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OfferRepository extends CrudRepository<Offer, Long>, PagingAndSortingRepository<Offer, Long> {

}
