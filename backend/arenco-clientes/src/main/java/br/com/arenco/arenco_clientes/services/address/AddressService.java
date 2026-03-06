package br.com.arenco.arenco_clientes.services.address;

import br.com.arenco.arenco_clientes.entities.AddressModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AddressService {
    Page<AddressModel> findAll(final Pageable pageable);

}
