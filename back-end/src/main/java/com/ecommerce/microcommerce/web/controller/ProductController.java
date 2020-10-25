package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.responses.ProductWithMargin;
import com.ecommerce.microcommerce.web.delegate.ProductNotesDelegate;
import com.ecommerce.microcommerce.web.exceptions.ProduitGratuitException;
import com.ecommerce.microcommerce.web.exceptions.ProduitIntrouvableException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Api(description = "API pour es opérations CRUD sur les produits.")
@RestController
public class ProductController {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductNotesDelegate productNotesDelegate;

    @ApiOperation(value = "Calculer les marges pour tous les produits")
    @GetMapping("AdminProduits")
    public List<ProductWithMargin> calculerMargeProduit() {
        List<ProductWithMargin> productWithMarginResponse = new ArrayList<>();
        Iterable<Product> produits = productDao.findAll();
        produits.forEach(product -> productWithMarginResponse.add(new ProductWithMargin(product)));
        return productWithMarginResponse;
    }

    //Récupérer la liste des produits
    @GetMapping("/Produits")
    public MappingJacksonValue listeProduits() {
        Iterable<Product> produits = productDao.findAll();
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
        MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);
        produitsFiltres.setFilters(listDeNosFiltres);
        return produitsFiltres;
    }

    //Récupérer la liste des produits
    @GetMapping("/Produits/Sort")
    public MappingJacksonValue trierProduitsParOrdreAlphabetique() {
        Iterable<Product> produits = productDao.findAllByOrderByNomAsc();
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
        MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);
        produitsFiltres.setFilters(listDeNosFiltres);
        return produitsFiltres;
    }

    //Récupérer un produit par son Id
    @ApiOperation(value = "Récupère un produit grâce à son ID à condition que celui-ci soit en stock!")
    @GetMapping("/Produits/{id}")
    public Product afficherUnProduit(@PathVariable int id) {
        Product produit = productDao.findById(id);

        if (produit == null)
            throw new ProduitIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");

        return produit;
    }

    // Récupérer une note pour un id de produit donné
    @ApiOperation(value = "Récupérer une note pour un id de produit donné !")
    @GetMapping("/Produits/{id}/Note")
    public String getNoteProduit(@PathVariable int id) {
        Product produit = productDao.findById(id);

        if (produit == null)
            throw new ProduitIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");

        return productNotesDelegate.callNotesService(id);
    }

    // Ajouter un produit
    @PostMapping("/Produits")
    public Product ajouterProduit(@Valid @RequestBody Product product) throws ProduitGratuitException {
        if (product.getPrix() <= 0)
            throw new ProduitGratuitException("Le produit ne peut pas avoir un prix inférieur ou égal à 0.");

        return productDao.save(product);
    }

    @DeleteMapping("/Produits/{id}")
    public void supprimerProduit(@PathVariable int id) {
        Product produit = productDao.findById(id);

        if (produit == null)
            throw new ProduitIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE. Écran Bleu si je pouvais.");

        productDao.delete(id);
    }

    @PutMapping("/Produits")
    public void updateProduit(@RequestBody Product product) {
        Product produit = productDao.findById(product.getId());

        if (produit == null)
            throw new ProduitIntrouvableException("Le produit avec l'id " + product.getId() + " est INTROUVABLE. Écran Bleu si je pouvais.");

        productDao.save(product);
    }

    // Pour les tests
    @GetMapping("/Test/Produits/{prix}")
    public List<Product> testeDeRequetes(@PathVariable int prix) {
        return productDao.chercherUnProduitCher(400);
    }
}
