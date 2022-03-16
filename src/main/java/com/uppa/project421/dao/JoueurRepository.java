package com.uppa.project421.dao;

import com.uppa.project421.entities.Joueur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface JoueurRepository extends JpaRepository<Joueur, Long> {

}
