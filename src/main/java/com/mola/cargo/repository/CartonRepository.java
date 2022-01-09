package com.mola.cargo.repository;

import com.mola.cargo.model.Carton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartonRepository extends JpaRepository<Carton, Long> {
}
