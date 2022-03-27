package com.uppa.project421.dao;

import com.uppa.project421.entities.Joueur;
import com.uppa.project421.entities.Partie;
import com.uppa.project421.entities.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface TourRepository extends JpaRepository<Tour, Long> {
    public Collection<Tour> findByJoueurAndPartieAndPhase(Joueur joueur, Partie partie, int phase);

}
