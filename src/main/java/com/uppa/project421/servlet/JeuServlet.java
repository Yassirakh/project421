package com.uppa.project421.servlet;

import com.uppa.project421.entities.Joueur;
import com.uppa.project421.services.JeuService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet("/game")
public class JeuServlet extends HttpServlet {
    @Autowired
    private JeuService jeuService;

    protected void authProcessGetRequest(
            HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String operation = req.getParameter("operation");
        if (operation.equals("auth")) {
            String auth_request = req.getParameter("status");
            if (auth_request!= null && auth_request.equals("auth-request")) {
                System.out.println(req.getParameter("mdp"));
                Collection<Joueur> joueur_auth = jeuService.joueurAuth(req.getParameter("pseudo"),req.getParameter("mdp"));
                if (!joueur_auth.isEmpty()) {
                    Joueur joueur = joueur_auth.iterator().next();
                    HttpSession session = req.getSession(true);
                    session.setAttribute("joueur", joueur);
                    req.setAttribute("bad_auth", false);
                    req.setAttribute("joueur", joueur);
                    req.getRequestDispatcher("auth.jsp").forward(req, resp);
                }
                else {
                    PrintWriter out = resp.getWriter();
                    req.setAttribute("bad_auth", true);
                    req.getRequestDispatcher("auth.jsp").forward(req, resp);
                }
            }
            else if (auth_request!= null && auth_request.equals("register-request")) {
                req.getParameter("sex");
                try {
                    Joueur joueur_registred = jeuService.joueurInscription(new Joueur(null, req.getParameter("pseudo"), req.getParameter("mdp"),  Integer.valueOf(req.getParameter("age")), req.getParameter("sexe"), req.getParameter("ville"), null, null));
                    HttpSession session = req.getSession(true);
                    session.setAttribute("joueur", joueur_registred);
                    req.setAttribute("bad_auth", false);
                    req.setAttribute("joueur", joueur_registred);
                    req.getRequestDispatcher("home.jsp").forward(req, resp);
                }
                catch (Exception e) {
                    req.setAttribute("bad_auth", true);
                    req.getRequestDispatcher("register.jsp").forward(req, resp);
                }
            }
            else {
                req.getRequestDispatcher("auth.jsp").forward(req, resp);
            }
        }
        else if (operation.equals("register")) {
            req.getRequestDispatcher("register.jsp").forward(req, resp);
        }
        else if (operation.equals("test1")) {
            req.setAttribute("stats", jeuService.statistiqueJoueur(jeuService.joueursChargement()));
            req.getRequestDispatcher("test1.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String operation = req.getParameter("operation");
        if (operation.equals("home")) {
            req.getRequestDispatcher("home.jsp").forward(req, resp);
        }
        else if (operation.equals("auth") || operation.equals("register")) {
            this.authProcessGetRequest(req, resp);

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String operation = req.getParameter("operation");
        if (operation.equals("home")) {
            req.getRequestDispatcher("home.jsp").forward(req, resp);
        }
        else if (operation.equals("auth") || operation.equals("register")) {
            this.authProcessGetRequest(req, resp);
        }
    }
}
