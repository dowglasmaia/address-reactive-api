package com.dowglasmaia.address.api.repository;

import com.dowglasmaia.address.api.document.AddressDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AddressRepository extends ReactiveMongoRepository<AddressDocument, String> {
}
