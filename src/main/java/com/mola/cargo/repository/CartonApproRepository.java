package com.mola.cargo.repository;

import com.mola.cargo.model.CartonAppro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartonApproRepository extends JpaRepository<CartonAppro, Long> {
}
