package com.uppa.project421.services;

import com.uppa.project421.dao.JoueurRepository;
import com.uppa.project421.dao.LanceRepository;
import com.uppa.project421.dao.PartieRepository;
import com.uppa.project421.dao.TourRepository;
import com.uppa.project421.entities.Joueur;
import com.uppa.project421.entities.Lance;
import com.uppa.project421.entities.Partie;
import com.uppa.project421.entities.Tour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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

    private Map<Integer[], Integer> combinaison;

    public JeuService() {
        this.combinaison = new HashMap<>();
        this.combinaison.put(new Integer[]{4,2,1}, 10);
        this.combinaison.put(new Integer[]{1,1,1}, 7);
        this.combinaison.put(new Integer[]{6,1,1}, 6);
        this.combinaison.put(new Integer[]{6,6,6}, 6);
        this.combinaison.put(new Integer[]{5,1,1}, 5);
        this.combinaison.put(new Integer[]{5,5,5}, 5);
        this.combinaison.put(new Integer[]{4,1,1}, 4);
        this.combinaison.put(new Integer[]{4,4,4}, 4);
        this.combinaison.put(new Integer[]{3,1,1}, 3);
        this.combinaison.put(new Integer[]{3,3,3}, 3);
        this.combinaison.put(new Integer[]{2,1,1}, 3);
        this.combinaison.put(new Integer[]{2,2,2}, 3);
        this.combinaison.put(new Integer[]{6,5,4}, 2);
        this.combinaison.put(new Integer[]{5,4,3}, 2);
        this.combinaison.put(new Integer[]{4,3,2}, 2);
        this.combinaison.put(new Integer[]{3,2,1}, 2);
        this.combinaison.put(new Integer[]{2,2,1}, 2);
    }

    public Joueur joueurInscription(Joueur joueur) {
        try {
            joueurRepository.save(joueur);
            return joueur;
        }
        catch (Exception e) {
            System.out.println("Error occurred");
            System.out.println(e.getMessage());

        }
        return joueur;
    }

    public Collection<Joueur> joueurAuth(String pseudo, String motdepasse) {
        try {
            return joueurRepository.findByPseudoAndMotdepasse(pseudo, motdepasse);
        }
        catch (Exception e) {
            return null;
        }
    }

    public Collection<Joueur> joueursChargement() {
        try {
            return joueurRepository.findAll();
        }
        catch (Exception e) {
            return null;
        }
    }


    public Collection<Joueur> joueurChargement(String pseudo, Long id) {
        if (pseudo != null) {
            try {
                return joueurRepository.findByPseudo(pseudo);
            }
            catch (Exception e) {
                return null;
            }
        }
        else if (id != null) {
            try {
                return joueurRepository.findByIdjoueur(id);
            }
            catch (Exception e) {
                return null;
            }
        }
        else {
            return null;
        }
    }

    public void ajoutJoueursPartie(Collection<Joueur> joueurs, Partie partie) {
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

    public Collection<Tour> tourChargement(Long id) {
        try {
            return tourRepository.findByIdtour(id);
        }
        catch (Exception e) {
            return null;
        }
    }

    public Collection<Partie> partieChargement(Long id) {
        try {
            return partieRepository.findByIdpartie(id);
        }
        catch (Exception e) {
            return null;
        }
    }

    public Tour ajoutTour(Tour tour, Tour tour_prec, Partie partie, Joueur joueur) {
        if (tour_prec != null) {
            tour.setJetons(tour_prec.getJetons());
        }
        joueur.addTour(tour);
        partie.addTour(tour);
        tour.setJoueur(joueur);
        tour.setPartie(partie);
        try {
            tourRepository.save(tour);
            joueurRepository.save(joueur);
            partieRepository.save(partie);
            return tour;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Tour ajoutJetons(Tour tour, int jetons) {
        tour.setJetons(tour.getJetons() + jetons);
        try {
            tourRepository.save(tour);
            return tour;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Tour retirerJetons(Tour tour, int jetons) {
        tour.setJetons(tour.getJetons() - jetons);
        try {
            tourRepository.save(tour);
            return tour;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Collection<Lance> lanceChargement(Long Id) {
        try {
            return lanceRepository.findByIdlance(Id);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Lance relanceDes(Lance old_lance, int pos_des) {
        Random r = new Random();
        Lance lance = old_lance;
        lance.setIdlance(null);
        if (pos_des == 1) {
            lance.setDesUn(r.nextInt((6 - 1) + 1) + 1);
        }
        else if (pos_des == 2) {
            lance.setDesDeux(r.nextInt((6 - 1) + 1) + 1);
        }
        else {
            lance.setDesTrois(r.nextInt((6 - 1) + 1) + 1);
        }
        try {
            lanceRepository.save(lance);
            return lance;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public Lance ajoutLance(Tour tour) {
        Random r = new Random();
        int des_un = r.nextInt((6 - 1) + 1) + 1;
        int des_deux = r.nextInt((6 - 1) + 1) + 1;
        int des_trois = r.nextInt((6 - 1) + 1) + 1;
        Lance lance = new Lance(null, des_un, des_deux, des_trois, tour);
        tour.addLance(lance);
        try {
            lanceRepository.save(lance);
            tourRepository.save(tour);
            return lance;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Map<Integer, Integer> debutDecharge(Collection<Integer> idsJoueurs) {
        Random r = new Random();
        Map<Integer, Integer> joueursDes = null;
        idsJoueurs.forEach(id ->{
            joueursDes.put(id, r.nextInt((6 - 1) + 1) + 1);
        });
        return joueursDes;
    }

    public Collection<Lance> departageDecharge(Collection<Lance> lances, Collection<Integer> desPos) {
        Random r = new Random();
        int i = 0;
        lances.forEach(lance ->{
            if (desPos.iterator().next() == 1) {
                lance.setDesUn(r.nextInt((6 - 1) + 1) + 1);
            }
            else if (desPos.iterator().next() == 2) {
                lance.setDesDeux(r.nextInt((6 - 1) + 1) + 1);
            }
            else {
                lance.setDesTrois(r.nextInt((6 - 1) + 1) + 1);
            }
        });
        try {
            lanceRepository.saveAll(lances);
            return lances;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public Map<String, Integer> jetonsCombinaisons(Collection<Lance> lances) {
        Map<String, Integer> jetons = new HashMap<String, Integer>();
        lances.forEach(lance -> {
            AtomicInteger found = new AtomicInteger();
            found.set(0);
            Integer[] des = new Integer[3];
            des[0] = lance.getDesUn();
            des[1] = lance.getDesDeux();
            des[2] = lance.getDesTrois();
            Arrays.sort(des, Collections.reverseOrder());
            combinaison.forEach((comb, jet) -> {
                if (comb[0] == des[0] && comb[1] == des[1] && comb[2] == des[2]) {
                //if (comb.equals(des)) {
                    System.out.println("equales");
                    jetons.put(lance.getTour().getJoueur().getPseudo(),jet);
                    found.set(1);
                }
            });
            if (found.get() == 0) {
                jetons.put(lance.getTour().getJoueur().getPseudo(),1);
            }
        });
        System.out.println("jetons");
        System.out.println(jetons.toString());

        return jetons;
    }

   /* public void creationTours(@NotNull Collection<Joueur> joueurs, Partie partie) {
        Collection<Lance> lances = new ArrayList<Lance>();
        Collection<Tour> tours = new ArrayList<Tour>();
        joueurs.forEach(jo -> {
            tours.add(this.ajoutTour(jo, partie));
        });
    }*/

    public Map<String, Statistique> statistiqueJoueur(Collection<Joueur> joueurs) {
        Map<String, Statistique> stats = new HashMap<String, Statistique>();
        AtomicInteger jetCharge = new AtomicInteger(0);
        AtomicInteger jetDecharge = new AtomicInteger(0);
        AtomicInteger cptCharge = new AtomicInteger(0);
        AtomicInteger cptDecharge = new AtomicInteger(0);
        joueurs.forEach(jo -> {
            jetCharge.set(0);
            jetDecharge.set(0);
            cptCharge.set(0);
            cptDecharge.set(0);
            Statistique stat = new Statistique();
            stat.setPartieJouees(jo.getPartiesCollection().size());
            stat.setPartieGagnees(0);
            jo.getPartiesCollection().forEach(par -> {
                ArrayList <Tour> chargeTours = new ArrayList<Tour>();
                ArrayList <Tour> dechargeTours = new ArrayList<Tour>();
                if (par.getJoueurGagnant() != null && par.getJoueurGagnant().getPseudo().equals(jo.getPseudo())) {
                    stat.setPartieGagnees(stat.getPartieGagnees() + 1);
                }
                par.getToursCollection().forEach(tour ->{
                    if (tour.getJoueur().getIdjoueur().equals(jo.getIdjoueur())) {
                        if (tour.getPhase() == 1) {
                            chargeTours.add(tour);
                            System.out.println(tour.getIdtour() + " " + tour.getJetons());
                        }
                        else {
                            dechargeTours.add(tour);
                        }
                    }
                });
                try {
                    jetCharge.set(chargeTours.get(chargeTours.size() -1).getJetons() + jetCharge.get());
                    cptCharge.incrementAndGet();
                }
                catch (Exception e) {
                    cptCharge.incrementAndGet();
                }
                try {
                    jetDecharge.set(dechargeTours.get(dechargeTours.size() -1).getJetons() + jetDecharge.get());
                    cptDecharge.incrementAndGet();
                }
                catch (Exception e) {
                    cptDecharge.incrementAndGet();
                }
            });
            try {
                stat.setPartieGagneesPrct((stat.getPartieGagnees()/stat.getPartieJouees())*100);
            }
            catch (Exception e) {
                stat.setPartieGagneesPrct(0);
            }
            try {
                stat.setJetonsChargePrct(jetCharge.get() / cptCharge.get());
            }
            catch (Exception e) {
                stat.setJetonsChargePrct(0);
            }
            try {
                stat.setJetonsDechargePrct(jetDecharge.get() / cptDecharge.get());
            }
            catch (Exception e) {
                stat.setJetonsDechargePrct(0);
            }
            stats.put(jo.getPseudo(), stat);
            System.out.println( jo.getIdjoueur() + " " + stat.toString());
        });
        return stats;
    }

    public List<Partie> statistiqueParties() {
        try {
            return partieRepository.findAll();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
