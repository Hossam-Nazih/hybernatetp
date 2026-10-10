package com.example;

import com.example.model.Produit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.List;
import org.h2.tools.Server;

public class App {

    public static void main(String[] args) {

        // On ouvre la console web de H2 afin d'inspecter les tables dans le navigateur
        try {
            Server.createWebServer("-web", "-webPort", "8083").start();
            System.out.println("Console H2 ouverte sur : http://localhost:8083");
        } catch (Exception e) {
            System.out.println("La console H2 n'a pas pu démarrer.");
            e.printStackTrace();
        }

        // Initialisation de JPA à partir de l'unité de persistance déclarée
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hibernate-demo");

        // Étape 1 : insertion des données de départ
        ajouterProduits(emf);

        // Étape 2 : relecture et affichage de ce qui est stocké
        afficherProduits(emf);

        // Fin du programme : on referme la fabrique
        emf.close();
    }

    private static void ajouterProduits(EntityManagerFactory emf) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // Données d'exemple : du matériel informatique et mobile
            Produit p1 = new Produit(
                    "ROG Strix G16",
                    new BigDecimal("15999.00"),
                    "PC portable gaming RTX 4060",
                    "ASUS",
                    "Ordinateurs",
                    12
            );
            p1.setCodeArticle("PRD-2026-0001");
            p1.setMasseKg(2.5);

            Produit p2 = new Produit(
                    "Galaxy S24",
                    new BigDecimal("8499.00"),
                    "Smartphone avec écran AMOLED",
                    "Samsung",
                    "Smartphones",
                    25
            );
            p2.setCodeArticle("PRD-2026-0002");
            p2.setMasseKg(0.167);

            Produit p3 = new Produit(
                    "MX Master 3S",
                    new BigDecimal("1099.00"),
                    "Souris sans fil ergonomique",
                    "Logitech",
                    "Accessoires",
                    40
            );
            p3.setCodeArticle("PRD-2026-0003");
            p3.setMasseKg(0.141);

            Produit p4 = new Produit(
                    "iPad Air",
                    new BigDecimal("7299.00"),
                    "Tablette pour le travail et les études",
                    "Apple",
                    "Tablettes",
                    0
            );
            p4.setCodeArticle("PRD-2026-0004");
            p4.setMasseKg(0.461);
            // Cet article est en rupture : on le sort de la vente
            p4.setEnVente(false);

            // Chaque objet est confié à JPA pour être sauvegardé
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.persist(p4);

            em.getTransaction().commit();

            System.out.println("Insertion terminée : 4 produits en base.");

        } catch (Exception e) {

            // En cas de problème, on défait tout ce qui a été fait dans la transaction
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            System.out.println("L'insertion des produits a échoué.");
            e.printStackTrace();

        } finally {
            em.close();
        }
    }

    private static void afficherProduits(EntityManagerFactory emf) {

        EntityManager em = emf.createEntityManager();

        try {
            // Requête JPQL qui ramène l'ensemble des produits
            List<Produit> produits = em.createQuery(
                    "SELECT p FROM Produit p", Produit.class
            ).getResultList();

            System.out.println("\n===== TOUS LES PRODUITS =====");

            for (Produit p : produits) {
                System.out.println(p);
            }

            // On ne garde que les articles réellement achetables
            System.out.println("\n===== ARTICLES ACHETABLES =====");

            for (Produit p : produits) {
                if (p.peutEtreVendu()) {
                    System.out.println(p.getDesignation() + " : " + p.getStockDispo() + " unités");
                }
            }

            // Calcul de la valeur du stock : tarif multiplié par la quantité, additionné
            BigDecimal valeurTotale = BigDecimal.ZERO;
            for (Produit p : produits) {
                valeurTotale = valeurTotale.add(
                        p.getTarif().multiply(BigDecimal.valueOf(p.getStockDispo()))
                );
            }
            System.out.println("\nValeur globale du stock : " + valeurTotale);

            // Recherche d'un produit grâce à son code article
            System.out.println("\n===== RECHERCHE PAR CODE ARTICLE =====");

            String codeCherche = "PRD-2026-0002";
            List<Produit> resultats = em.createQuery(
                    "SELECT p FROM Produit p WHERE p.codeArticle = :code", Produit.class
            ).setParameter("code", codeCherche).getResultList();

            if (!resultats.isEmpty()) {
                System.out.println("Produit identifié : " + resultats.get(0));
            } else {
                System.out.println("Pas de produit pour le code : " + codeCherche);
            }

        } finally {
            // Clôture de la session utilisée pour la lecture
            em.close();
        }
    }
}