package com.example.tradearea.controller;

import com.example.tradearea.entity.Offer;
import com.example.tradearea.response.ResponsePage;
import com.example.tradearea.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("offers")
public class OfferController {

    private final OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping
    public HttpStatus create(@RequestBody Offer offer) {
        offerService.add(offer);
        return HttpStatus.CREATED;
    }

    @GetMapping
    public ResponsePage<Offer> read(
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = true, defaultValue = "updated") String sortBy,
            @RequestParam(required = true, defaultValue = "false") Boolean ascending) {

        final Page<Offer> offers = offerService.getAll(pageNum, pageSize, sortBy, ascending);

        return  new ResponsePage<>(offers);
    }

    @GetMapping(value = "/{id}")
    public Offer read(@PathVariable(name = "id") long id) {
        final Offer offer = offerService.getById(id);
        return offer;
    }

    @PutMapping
    public HttpStatus update(@RequestBody Offer offer) {
        offerService.edit(offer);
        return HttpStatus.OK;
    }

    @DeleteMapping(value = "/{id}")
    public HttpStatus delete(@PathVariable(name = "id") long id) {
        offerService.delete(id);
        return HttpStatus.OK;
    }
}
