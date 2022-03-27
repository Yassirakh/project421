package com.uppa.project421.servlet;

import com.uppa.project421.services.JeuService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/test")
public class JeuServlet extends HttpServlet {
    @Autowired
    private JeuService jeuService;

    protected void processRequest(
            HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String operation = req.getParameter("operation");

        if (operation.equals("test")) {
            req.setAttribute("parties", jeuService.statistiqueParties());
            req.getRequestDispatcher("test.jsp").forward(req, resp);
        } else if  (operation.equals("test1")) {
            req.setAttribute("stats", jeuService.statistiqueJoueur(jeuService.joueurChargement()));
            req.getRequestDispatcher("test1.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }

}
