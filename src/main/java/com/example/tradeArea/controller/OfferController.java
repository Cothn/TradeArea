package com.example.tradeArea.controller;

import com.example.tradeArea.entity.Offer;
import com.example.tradeArea.response.ResponsePage;
import com.example.tradeArea.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


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
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = true, defaultValue = "updated") String sortBy,
            @RequestParam(required = true, defaultValue = "false") Boolean ascending) {

        final Page<Offer> offers = offerService.getAll(pageNum-1, pageSize, sortBy, ascending);

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
        offerService.delete(offerService.getById(id));
        return HttpStatus.OK;
    }
}
