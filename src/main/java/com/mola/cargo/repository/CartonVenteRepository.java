package com.mola.cargo.repository;

import com.mola.cargo.model.CartonVente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartonVenteRepository extends JpaRepository<CartonVente, Long> {

}
