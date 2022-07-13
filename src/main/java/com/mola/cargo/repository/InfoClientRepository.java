package com.mola.cargo.repository;

import com.mola.cargo.model.InfoClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoClientRepository extends JpaRepository<InfoClient, Long> {
}
