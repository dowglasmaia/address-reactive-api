package com.dowglasmaia.address.api.service.impl;

import br.com.dowglasmaia.openapi.model.AddressIdResponse;
import br.com.dowglasmaia.openapi.model.AddressRequest;
import br.com.dowglasmaia.openapi.model.AddressResponse;
import com.dowglasmaia.address.api.document.AddressDocument;
import com.dowglasmaia.address.api.repository.AddressRepository;
import com.dowglasmaia.address.api.service.AddressService;
import com.dowglasmaia.address.api.service.mapper.AddressMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository repository;
    @Autowired
    private AddressMapper mapper;
    @Override
    public Mono<AddressIdResponse> insert(AddressRequest request) {
        log.info("Start Method insert Address");

        AddressDocument documentRequest = mapper.toAddressDocument(request);
        return repository.save(documentRequest).map(
                saveDocument -> new AddressIdResponse().id(saveDocument.getId())
        );
    }

    @Override
    public Mono<AddressResponse> findByZipCode(String zipCode) {
        log.info("Start Method findByZipCode with zipCode: {}",zipCode);

        return repository.findByZip(zipCode)
                .map( document -> mapper.toAddressResponse(document));
    }
}
