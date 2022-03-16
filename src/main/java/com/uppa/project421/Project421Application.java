package com.uppa.project421;

import com.uppa.project421.dao.JoueurRepository;
import com.uppa.project421.entities.Joueur;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Project421Application {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(Project421Application.class, args);
        JoueurRepository joueurRepository = context.getBean(JoueurRepository.class);
        //joueurRepository.save(new Joueur(null, "yasdddsir", "mdp", 25, "M", "pau"));
        System.out.println("here1");
        joueurRepository.findAll().forEach(jo -> {
            System.out.println("here2");
            System.out.println(jo.toString());
        });
    }

}
