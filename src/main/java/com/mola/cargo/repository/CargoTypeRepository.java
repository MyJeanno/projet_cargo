package com.mola.cargo.repository;

import com.mola.cargo.model.CargoType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargoTypeRepository extends JpaRepository<CargoType, Long> {
}
