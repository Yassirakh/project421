package com.uppa.project421.controller;

import com.uppa.project421.entities.Joueur;
import com.uppa.project421.entities.Partie;
import com.uppa.project421.services.JeuService;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
        Long id_partie = partie.getId_partie();
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
}