package com.uppa.project421.controller;

import com.uppa.project421.entities.Joueur;
import com.uppa.project421.entities.Lance;
import com.uppa.project421.entities.Partie;
import com.uppa.project421.entities.Tour;
import com.uppa.project421.services.JeuService;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.JobExecutionEvent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static com.uppa.project421.websocket.WebSocketEventListener.onlinePlayers;

import static com.uppa.project421.websocket.WebSocketEventListener.inGamePlayersId;

import static com.uppa.project421.websocket.WebSocketEventListener.games;


@Controller
public class JeuController {
    @Autowired
    JeuService jeuService;
    @RequestMapping("/home")
    public String test() {
        return "index";
    }

    @Autowired
    HttpSession httpSession = null;


    @RequestMapping("/auth")
    public String auth() {
        return "auth";
    }

    @RequestMapping(value="/auth-request", method = RequestMethod.POST)
    public String authRequest(Model model, HttpServletRequest req, HttpServletResponse resp) {
        Collection<Joueur> joueur_auth = jeuService.joueurAuth(req.getParameter("pseudo"),req.getParameter("mdp"));
        if (!joueur_auth.isEmpty()) {
            Joueur joueur = joueur_auth.iterator().next();

            if (httpSession == null) {
                httpSession = req.getSession(true);
            }

            httpSession.setAttribute("joueur", joueur);
            req.setAttribute("joueur", joueur);

            model.addAttribute("name", joueur.getPseudo());
            model.addAttribute("userid", joueur.getIdjoueur());
            return "menu";
        }
        else {
            model.addAttribute("bad_auth", true);
            return "auth";
        }
    }

    @RequestMapping("/register")
    public String register() {
        return "register";
    }

    @RequestMapping(value="/register-request", method = RequestMethod.POST)
    public String registerRequest(Model model, HttpServletRequest req, HttpServletResponse resp) {
        Collection<Joueur> joueur_find = jeuService.joueurChargement(req.getParameter("pseudo"), null);
        if (joueur_find.isEmpty()) {
            Joueur joueur_registred = jeuService.joueurInscription(new Joueur(null, req.getParameter("pseudo"), req.getParameter("mdp"),  Integer.valueOf(req.getParameter("age")), req.getParameter("sexe"), req.getParameter("ville"), null, null));



            if (httpSession == null) {
                httpSession = req.getSession(true);
            }

            httpSession.setAttribute("joueur", joueur_registred);
            req.setAttribute("joueur", joueur_registred);

            model.addAttribute("name", joueur_registred.getPseudo());
            model.addAttribute("id", joueur_registred.getIdjoueur());
            return "menu";
        }
        else {
            req.setAttribute("bad_auth", true);
            return "register";
        }
    }

    @RequestMapping("/chat")
    public String chat(Model model, HttpServletRequest req, HttpServletResponse resp) {
        model.addAttribute("username", ((Joueur)httpSession.getAttribute("joueur")).getPseudo());
        model.addAttribute("userId", ((Joueur)httpSession.getAttribute("joueur")).getIdjoueur());
        return "chat";
    }

    @RequestMapping("/lobby")
    public String lobby(Model model, HttpServletRequest req, HttpServletResponse resp) {
        model.addAttribute("users", onlinePlayers);
        return "lobby";
    }

    @RequestMapping(value = "/refresh-players", method = RequestMethod.POST)
    public @ResponseBody
    String processCreateGame() {
        Map<Integer, String> availablePlayers = new HashMap<Integer, String>();
        for (Map.Entry<Integer, String> entry : onlinePlayers.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
            if (!inGamePlayersId.contains(entry.getKey())) {
                availablePlayers.put(entry.getKey(), entry.getValue());
            }
        }
        JSONObject json = new JSONObject(availablePlayers);
        return json.toString();
    }

    @RequestMapping(value = "/createGame", method = RequestMethod.POST)
    public @ResponseBody String createGame(@RequestBody String[] data) {
        ArrayList<Joueur> players = new ArrayList<Joueur>();
        for (String id : data) {
            players.add(jeuService.joueurChargement(null, Long.parseLong(id)).iterator().next());
        }
        Partie partie = new Partie(null, new Date(System.currentTimeMillis()), null, null, null, null);
        jeuService.ajoutJoueursPartie(players, partie);
        Long id_partie = partie.getIdpartie();
        games.put(id_partie, players);
        return id_partie.toString();
    }

    @RequestMapping(value = "/startgame")
    @CrossOrigin(origins="*")
    public String startgame(@ModelAttribute(value="id") Integer userid, @ModelAttribute(value="id_partie") Integer partieid, Model model, HttpServletRequest req, HttpServletResponse resp) {
        inGamePlayersId.add(userid);
        model.addAttribute("id_partie", partieid);
        return "game";
    }

    @RequestMapping(value = "/listPlayerGame", method = RequestMethod.POST)
    public @ResponseBody Object[] listPlayerGame(@RequestBody Long id_partie) {
        ArrayList<String> gamePlayers = new ArrayList<String>();
        games.get(id_partie).forEach(play -> {
            gamePlayers.add(play.getPseudo());
        });
        return gamePlayers.toArray();
    }


    @RequestMapping(value = "/createChargeTour", method = RequestMethod.POST)
    public @ResponseBody String createChargeTour(@RequestBody ArrayList<Long> joueur_partie_tour) {
        Tour tour_prec_collec = null;
        if (joueur_partie_tour.size() >= 3) {
                tour_prec_collec = jeuService.tourChargement(joueur_partie_tour.get(2)).iterator().next();
        }
        Partie partie = jeuService.partieChargement(joueur_partie_tour.get(1)).iterator().next();
        Joueur joueur = jeuService.joueurChargement(null, joueur_partie_tour.get(0)).iterator().next();
        Tour tour = new Tour(null, 0, 0, null, 1, null, null, null);
        tour = jeuService.ajoutTour(tour, tour_prec_collec, partie, joueur);

        return tour.getIdtour().toString();
    }

    @RequestMapping(value = "/createChargeLance", method = RequestMethod.POST)
    public @ResponseBody Object[] createChargeLance(@RequestBody Long tourId) {
        Tour tour = jeuService.tourChargement(tourId).iterator().next();
        Lance lance = jeuService.ajoutLance(tour);
        ArrayList<Integer> lance_data = new ArrayList<Integer>();
        lance_data.add(lance.getDesUn());
        lance_data.add(lance.getDesDeux());
        lance_data.add(lance.getDesTrois());
        lance_data.add(lance.getIdlance().intValue());
        lance_data.add(tour.getJoueur().getIdjoueur().intValue());
        lance_data.add(tour.getPartie().getIdpartie().intValue());
        return lance_data.toArray();
    }

    @RequestMapping(value = "/combinaisonsLances", method = RequestMethod.POST)
    public @ResponseBody String combinaisonsLances(@RequestBody ArrayList<Long> data) {
        Collection<Lance> lances = new ArrayList<Lance>();
        data.forEach(id -> {
            lances.add(jeuService.lanceChargement(id).iterator().next());

        });

        Map<String, Integer> jeuCombinaison = jeuService.jetonsCombinaisons(lances);
        JSONObject json = new JSONObject(jeuCombinaison);
        System.out.println(json.toString());
        return json.toString();
    }


    @RequestMapping(value = "/addTokenToTour", method = RequestMethod.POST)
    public @ResponseBody String addTokenToTour(@RequestBody ArrayList<Long> data) {
        System.out.println("add token to tour " + data.get(1));
        Tour tour = jeuService.tourChargement(data.get(1)).iterator().next();
        jeuService.ajoutJetons(tour, data.get(0).intValue());
        return "success";
    }


    @RequestMapping(value = "/removeFromInGamePlayers", method = RequestMethod.POST)
    public String removeFromInGamePlayers(@RequestBody Integer plalyerID) {
        inGamePlayersId.removeAll(Arrays.asList(plalyerID));
        return "lobby";
    }

    @RequestMapping("/stat1")
    public String stat1(Model model, HttpServletRequest req, HttpServletResponse resp) {
        model.addAttribute("stats", jeuService.statistiqueJoueur(jeuService.joueursChargement()));
        return "stat1";
    }

    @RequestMapping("/stat2")
    public String stat2(Model model, HttpServletRequest req, HttpServletResponse resp) {
        model.addAttribute("stats", jeuService.statistiqueParties());
        return "stat2";
    }

    @RequestMapping("/menu")
    public String menu(Model model, HttpServletRequest req, HttpServletResponse resp) {
        Joueur joueur = (Joueur) httpSession.getAttribute("joueur");

        model.addAttribute("name", joueur.getPseudo());
        model.addAttribute("userid", joueur.getIdjoueur());
        return "menu";
    }
}