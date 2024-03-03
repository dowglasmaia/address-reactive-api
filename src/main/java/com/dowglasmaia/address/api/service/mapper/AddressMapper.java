package com.dowglasmaia.address.api.service.mapper;

import br.com.dowglasmaia.openapi.model.AddressRequest;
import br.com.dowglasmaia.openapi.model.AddressResponse;
import com.dowglasmaia.address.api.document.AddressDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDocument toAddressDocument(AddressRequest request);

    AddressResponse toAddressResponse(AddressDocument document);

}
