package com.uppa.project421.controller;

import com.uppa.project421.entities.Joueur;
import com.uppa.project421.services.JeuService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

import static com.uppa.project421.websocket.WebSocketEventListener.onlinePlayers;


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
            model.addAttribute("userid", joueur.getId_joueur());
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
        Collection<Joueur> joueur_find = jeuService.joueurChargement(req.getParameter("pseudo"));
        if (joueur_find.isEmpty()) {
            Joueur joueur_registred = jeuService.joueurInscription(new Joueur(null, req.getParameter("pseudo"), req.getParameter("mdp"),  Integer.valueOf(req.getParameter("age")), req.getParameter("sexe"), req.getParameter("ville"), null, null));



            if (httpSession == null) {
                httpSession = req.getSession(true);
            }

            httpSession.setAttribute("joueur", joueur_registred);
            req.setAttribute("joueur", joueur_registred);

            model.addAttribute("name", joueur_registred.getPseudo());
            model.addAttribute("id", joueur_registred.getId_joueur());
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
        model.addAttribute("userId", ((Joueur)httpSession.getAttribute("joueur")).getId_joueur());
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
        JSONObject json = new JSONObject(onlinePlayers);
        return json.toString();
    }

    @RequestMapping("/startgame")
    public void startGame(@RequestBody String data, Model model, HttpServletRequest req, HttpServletResponse resp) {
        //model.addAttribute("users", onlinePlayers);
        System.out.println(data);
        //return "lobby";
    }
}