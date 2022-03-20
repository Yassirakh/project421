package com.uppa.project421.services;

import com.uppa.project421.dao.JoueurRepository;
import com.uppa.project421.dao.LanceRepository;
import com.uppa.project421.dao.PartieRepository;
import com.uppa.project421.dao.TourRepository;
import com.uppa.project421.entities.Joueur;
import com.uppa.project421.entities.Partie;
import com.uppa.project421.entities.Tour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class JeuService {
    @Autowired
    private JoueurRepository joueurRepository;
    @Autowired
    private LanceRepository lanceRepository;
    @Autowired
    private PartieRepository partieRepository;
    @Autowired
    private TourRepository tourRepository;

    public void joueurInscription(Joueur joueur) {
        try {
            joueurRepository.save(joueur);
        }
        catch (Exception e) {
            System.out.println("Error occurred");
            System.out.println(e.getMessage());

        }
    }

    public Collection<Joueur> joueurAuth(String pseudo, String motdepasse) {
        try {
            return joueurRepository.findByPseudoAndMotdepasse(pseudo, motdepasse);
        }
        catch (Exception e) {
            return null;
        }
    }

    public void ajoutJoueursPartie(ArrayList<Joueur> joueurs, Partie partie) {
        joueurs.forEach(jo -> {
            try {
                partie.addJoueur(jo);
                jo.addPartie(partie);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        try {
            partieRepository.save(partie);
            joueurRepository.saveAll(joueurs);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Partie ajoutGagnantPartie(Joueur joueur, Partie partie) {
        try {
            partie.setJoueurGagnant(joueur);
            partieRepository.save(partie);
        }
        catch (Exception e) {
            System.out.println("Error occurred");
            System.out.println(e.getMessage());
        }
        return partie;
    }

    public Tour ajoutTour(Tour tour, Partie partie, Joueur joueur) {
        joueur.addTour(tour);
        partie.addTour(tour);
        tour.setJoueur(joueur);
        tour.setPartie(partie);
        try {
            tourRepository.save(tour);
            joueurRepository.save(joueur);
            partieRepository.save(partie);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return tour;
    }


}
