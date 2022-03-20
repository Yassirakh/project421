package com.uppa.project421;

import com.uppa.project421.dao.JoueurRepository;
import com.uppa.project421.dao.LanceRepository;
import com.uppa.project421.dao.PartieRepository;
import com.uppa.project421.dao.TourRepository;
import com.uppa.project421.entities.Joueur;
import com.uppa.project421.entities.Partie;
import com.uppa.project421.services.JeuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@SpringBootApplication
public class Project421Application implements CommandLineRunner {

    @Autowired
    private JeuService jeuService;


    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(Project421Application.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        Collection<Joueur> joueurs = jeuService.joueurAuth("buai", "mdp");
        Joueur joueur = joueurs.iterator().next();
        System.out.println(joueur.getPseudo());

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
