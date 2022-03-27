package com.uppa.project421;

import com.uppa.project421.dao.JoueurRepository;
import com.uppa.project421.dao.LanceRepository;
import com.uppa.project421.dao.PartieRepository;
import com.uppa.project421.dao.TourRepository;
import com.uppa.project421.entities.Joueur;
import com.uppa.project421.entities.Lance;
import com.uppa.project421.entities.Partie;
import com.uppa.project421.entities.Tour;
import com.uppa.project421.services.JeuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


@ServletComponentScan
@SpringBootApplication
public class Project421Application implements CommandLineRunner {

    @Autowired
    private JeuService jeuService;


    public static void main(String[] args) {

        SpringApplication.run(Project421Application.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
       /* Collection<Partie> statsPartie;
        statsPartie = jeuService.statistiqueParties();
        Joueur joueur = jeuService.joueurInscription(new Joueur(null, "XXXXXXXXXX", "mdp", 25, "M", "pau", null, null));
        Collection <Joueur> coljo = new ArrayList<Joueur>();
        coljo.add(joueur);
        Partie partie = new Partie(null, new Date(2022,0,9), new Date(2022), null, null, null);
        jeuService.ajoutJoueursPartie(coljo,partie);
        jeuService.ajoutGagnantPartie(coljo.iterator().next(), partie);*/
       /* ArrayList<Joueur> joueurs = new ArrayList<>();
        ArrayList<Tour> tours = new ArrayList<>();
        joueurs.add(jeuService.joueurAuth("buai", "mdp").iterator().next());
        joueurs.add(jeuService.joueurAuth("busni2", "mdp").iterator().next());
        Partie partie = new Partie(null, new Date(2022,0,9), new Date(2022), null, null, null);
        jeuService.ajoutJoueursPartie(joueurs, partie);
        System.out.println("vvv");
        joueurs.forEach(jo -> {
            //tours.add(jeuService.ajoutTour(new Tour(null, 0, 0, null, 1, jo, partie, null), null, partie, jo));
            Tour tour = new Tour(null, 0, 0, null, 1, null, null, null);
            jeuService.ajoutTour(tour, null, partie, jo);
            Lance lance1 = jeuService.ajoutLance(tour);
            Lance lance2 = jeuService.ajoutLance(tour);
            Lance lance3 = jeuService.ajoutLance(tour);
            System.out.println("Joueur : " + jo.getPseudo());
            jo.getToursCollection().forEach(tr -> {
                System.out.println("Tour : " + tr.getId_tour());

                tr.getLancesCollection().forEach(lan -> {
                    System.out.println("Dés 1 : " + lan.getDesUn());
                    System.out.println("Dés 2 : " + lan.getDesDeux());
                    System.out.println("Dés 3 : " + lan.getDesTrois());

                });
            });
        });
        System.out.println("PARTIE :");
        partie.getToursCollection().forEach(tr -> {
            System.out.println(tr.getId_tour());
        });*/
        /*JoueurRepository joueurRepository = context.getBean(JoueurRepository.class);
        LanceRepository lanceRepository = context.getBean(LanceRepository.class);
        PartieRepository partieRepository = context.getBean(PartieRepository.class);
        TourRepository tourRepository = context.getBean(TourRepository.class);
        //JeuService jeuServiceImp = new JeuServiceImp(joueurRepository, lanceRepository, partieRepository, tourRepository);
        //PartieRepository partieRepository = context.getBean(PartieRepository.class);
        Joueur jo1 = new Joueur(null, "buai", "mdp", 25, "M", "pau", null, null);
        Joueur jo2 = new Joueur(null, "buscdcsai2", "mdp", 25, "M", "pau", null, null);
        Joueur jo3 = new Joueur(null, "xddef", "mdp", 25, "M", "pau", null, null)  ;

        try {
            jeuService.joueurInscription(jo1);
            jeuService.joueurInscription(jo2);
            jeuService.joueurInscription(jo3);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        Partie par1 = new Partie(null, new Date(2022), new Date(2022), null, null, null);
        ArrayList<Joueur> collectionJoueurs = new ArrayList<>();
        collectionJoueurs.add(jo1);
        collectionJoueurs.add(jo2);
        collectionJoueurs.add(jo3);
        try {
            jeuService.ajoutJoueursPartie(collectionJoueurs, par1);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        jo1.getPartiesCollection().forEach(par -> {
            System.out.println(par.getId_partie());
        });*/
        /*System.out.println("test");
        Joueur jo1 = new Joueur(null, "busni", "mdp", 25, "M", "pau", null, null);
        Joueur jo2 = new Joueur(null, "busni2", "mdp", 25, "M", "pau", null, null);
        Joueur jo3 = new Joueur(null, "busni3", "mdp", 25, "M", "pau", null, null);
        joueurRepository.save(jo1);
        Partie par1 = new Partie(null, new Date(2022), new Date(2022), null, null, null);
        Partie par2 = new Partie(null, new Date(2022), new Date(2022), null, null, null);
        jo1.addPartie(par1);
        //jo1.addPartie(par2);
        par1.addJoueur(jo1);
        par2.addJoueur(jo1);
        partieRepository.save(par1);
        partieRepository.save(par2);
        joueurRepository.save(jo1);
        //partieRepository.save(new Partie(null, new Date(2022), new Date(2022), null, null, null));
        //System.out.println(joueurRepository.findByPseudo("yasdddsir").toString());
        joueurRepository.findAll().forEach(jo -> {
            System.out.println(jo.getPseudo());
        });
        System.out.println(jo1.getPseudo());
        jo1.getPartiesCollection().forEach(par -> {
            System.out.println(par.getId_partie());
        });
        //System.out.println(par1.getJoueursCollection());*/

    }
}
