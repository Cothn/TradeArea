package com.example.tradearea.controller;

import com.example.tradearea.entity.Offer;
import com.example.tradearea.response.ResponsePage;
import com.example.tradearea.service.OfferService;
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
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody Offer offer) {
        return offerService.add(offer);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponsePage<Offer> read(
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = true, defaultValue = "updated") String sortBy,
            @RequestParam(required = true, defaultValue = "false") Boolean ascending) {

        final Page<Offer> offers = offerService.getAll(pageNum, pageSize, sortBy, ascending);

        return  new ResponsePage<>(offers);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Offer read(@PathVariable(name = "id") long id) {
        final Offer offer = offerService.getById(id);
        return offer;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody Offer offer) {
        offerService.edit(offer);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable(name = "id") long id) {
        offerService.delete(id);
    }
}
