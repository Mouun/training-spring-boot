package com.ecommerce.microcommerce;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.responses.ProductWithMargin;
import com.ecommerce.microcommerce.web.controller.ProductController;
import com.ecommerce.microcommerce.web.delegate.ProductNotesDelegate;
import com.ecommerce.microcommerce.web.exceptions.ProduitGratuitException;
import com.ecommerce.microcommerce.web.exceptions.ProduitIntrouvableException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MicrocommerceApplication.class)
@WebMvcTest(controllers = ProductController.class)
public class MicrocommerceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductDao productDao;

    @MockBean
    private ProductNotesDelegate productNotesDelegate;

    private List<Product> products;

    @Before
    public void setUp() {
        products = new ArrayList<>();
        products.add(new Product(1, "Ordinateur portable", 350, 120));
        products.add(new Product(2, "Aspirateur Robot", 500, 200));
        products.add(new Product(3, "Table de Ping Pong", 750, 400));
    }

    @Test
    public void calculerMargeProduitSouldReturnAllProductsWithMargin() throws Exception {
        List<ProductWithMargin> productsWithMargin = new ArrayList<>(Arrays.asList(
                new ProductWithMargin(products.get(0)),
                new ProductWithMargin(products.get(1)),
                new ProductWithMargin(products.get(2))
        ));

        given(productDao.findAll()).willReturn(products);

        this.mockMvc.perform(get("/AdminProduits"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[1].product.id").value(productsWithMargin.get(1).getProduct().getId()))
                .andExpect(jsonPath("$[1].product.nom").value(productsWithMargin.get(1).getProduct().getNom()))
                .andExpect(jsonPath("$[1].product.prix").value(productsWithMargin.get(1).getProduct().getPrix()))
                .andExpect(jsonPath("$[1].product.prixAchat").value(productsWithMargin.get(1).getProduct().getPrixAchat()))
                .andExpect(jsonPath("$[1].margin").value(productsWithMargin.get(1).getMargin()))
                .andReturn();
    }

    @Test
    public void listeProduitsShouldReturnAllProducts() throws Exception {
        given(productDao.findAll()).willReturn(products);

        this.mockMvc.perform(get("/Produits"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0].id").value(products.get(0).getId()))
                .andExpect(jsonPath("$[0].nom").value(products.get(0).getNom()))
                .andExpect(jsonPath("$[0].prix").value(products.get(0).getPrix()))
                .andExpect(jsonPath("$[0].prixAchat").value(products.get(0).getPrixAchat()))
                .andReturn();
    }

    @Test
    public void trierProduitsParOrdreAlphabetiqueShouldReturnAllProductsSortedByNameAlphabetically() throws Exception {
        List<Product> sortedList = products
                .stream()
                .sorted(Comparator.comparing(Product::getNom))
                .collect(Collectors.toList());

        given(productDao.findAllByOrderByNomAsc()).willReturn(sortedList);

        this.mockMvc.perform(get("/Produits/Sort"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0].id").value(sortedList.get(0).getId()))
                .andExpect(jsonPath("$[0].nom").value(sortedList.get(0).getNom()))
                .andExpect(jsonPath("$[0].prix").value(sortedList.get(0).getPrix()))
                .andExpect(jsonPath("$[0].prixAchat").value(sortedList.get(0).getPrixAchat()))
                .andReturn();
    }

    @Test
    public void afficherUnProduitShouldReturnTheProductCorrespondingToTheGivenId() throws Exception {
        given(productDao.findById(2)).willReturn(products.get(1));

        this.mockMvc.perform(get("/Produits/{id}", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(products.get(1).getId()))
                .andExpect(jsonPath("$.nom").value(products.get(1).getNom()))
                .andExpect(jsonPath("$.prix").value(products.get(1).getPrix()))
                .andExpect(jsonPath("$.prixAchat").value(products.get(1).getPrixAchat()))
                .andReturn();
    }

    @Test
    public void afficherUnProduitShouldThrowProduitIntrouvableExceptionWhenAWrongIdIsGiven() throws Exception {
        this.mockMvc.perform(get("/Produits/{id}", 2))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ProduitIntrouvableException))
                .andExpect(result -> assertEquals("Le produit avec l'id 2 est INTROUVABLE. Écran Bleu si je pouvais.", result.getResolvedException().getMessage()))
                .andReturn();
    }

    @Test
    public void ajouterProduitShouldCorrectlyAddANewProduct() throws Exception {
        Product newProduct = new Product(4, "iPhone 12 Pro", 1200, 1050);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(newProduct);

        given(productDao.save(newProduct)).willReturn(newProduct);
        given(productDao.findById(4)).willReturn(newProduct);

        this.mockMvc.perform(post("/Produits").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        this.mockMvc.perform(get("/Produits/{id}", 4))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(newProduct.getId()))
                .andExpect(jsonPath("$.nom").value(newProduct.getNom()))
                .andExpect(jsonPath("$.prix").value(newProduct.getPrix()))
                .andExpect(jsonPath("$.prixAchat").value(newProduct.getPrixAchat()))
                .andReturn();
    }

    @Test
    public void ajouterProduitShouldThrowProduitGratuitExceptionWhenAPriceOfZeroIsGiven() throws Exception {
        Product newProduct = new Product(4, "iPhone 12 Pro", 0, 1050);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(newProduct);

        this.mockMvc.perform(post("/Produits").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ProduitGratuitException))
                .andExpect(result -> assertEquals("Le produit ne peut pas avoir un prix inférieur ou égal à 0.", result.getResolvedException().getMessage()))
                .andReturn();
    }

    @Test
    public void supprimerProduitShouldCorrectlyDeleteAProduct() throws Exception {
        this.mockMvc.perform(delete("/Produits/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        this.mockMvc.perform(get("/Produits/{id}", 1))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ProduitIntrouvableException))
                .andExpect(result -> assertEquals("Le produit avec l'id 1 est INTROUVABLE. Écran Bleu si je pouvais.", result.getResolvedException().getMessage()))
                .andReturn();
    }

    @Test
    public void updateProduitShouldCorrectlyUpdateAProduct() throws Exception {
        Product updatedProduct = new Product(1, "Ordinateur portable" , 300, 100);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(updatedProduct);

        given(productDao.save(updatedProduct)).willReturn(updatedProduct);
        given(productDao.findById(1)).willReturn(updatedProduct);

        this.mockMvc.perform(put("/Produits").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        this.mockMvc.perform(get("/Produits/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedProduct.getId()))
                .andExpect(jsonPath("$.nom").value(updatedProduct.getNom()))
                .andExpect(jsonPath("$.prix").value(updatedProduct.getPrix()))
                .andExpect(jsonPath("$.prixAchat").value(updatedProduct.getPrixAchat()))
                .andReturn();
    }
}