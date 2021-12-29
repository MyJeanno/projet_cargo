package com.mola.cargo.repository;

import com.mola.cargo.model.Piece;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PieceRepository extends JpaRepository<Piece, Long> {
}
