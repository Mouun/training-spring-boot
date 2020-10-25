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
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

    @Mock
    private RestTemplate restTemplate;

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

        when(productDao.findAll()).thenReturn(products);

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

        verify(productDao, times(1)).findAll();
    }

    @Test
    public void listeProduitsShouldReturnAllProducts() throws Exception {
        when(productDao.findAll()).thenReturn(products);

        this.mockMvc.perform(get("/Produits"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0].id").value(products.get(0).getId()))
                .andExpect(jsonPath("$[0].nom").value(products.get(0).getNom()))
                .andExpect(jsonPath("$[0].prix").value(products.get(0).getPrix()))
                .andExpect(jsonPath("$[0].prixAchat").value(products.get(0).getPrixAchat()))
                .andReturn();

        verify(productDao, times(1)).findAll();
    }

    @Test
    public void trierProduitsParOrdreAlphabetiqueShouldReturnAllProductsSortedByNameAlphabetically() throws Exception {
        List<Product> sortedList = products
                .stream()
                .sorted(Comparator.comparing(Product::getNom))
                .collect(Collectors.toList());

        when(productDao.findAllByOrderByNomAsc()).thenReturn(sortedList);

        this.mockMvc.perform(get("/Produits/Sort"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0].id").value(sortedList.get(0).getId()))
                .andExpect(jsonPath("$[0].nom").value(sortedList.get(0).getNom()))
                .andExpect(jsonPath("$[0].prix").value(sortedList.get(0).getPrix()))
                .andExpect(jsonPath("$[0].prixAchat").value(sortedList.get(0).getPrixAchat()))
                .andReturn();

        verify(productDao, times(1)).findAllByOrderByNomAsc();
    }

    @Test
    public void afficherUnProduitShouldReturnTheProductCorrespondingToTheGivenId() throws Exception {
        when(productDao.findById(2)).thenReturn(products.get(1));

        this.mockMvc.perform(get("/Produits/{id}", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(products.get(1).getId()))
                .andExpect(jsonPath("$.nom").value(products.get(1).getNom()))
                .andExpect(jsonPath("$.prix").value(products.get(1).getPrix()))
                .andExpect(jsonPath("$.prixAchat").value(products.get(1).getPrixAchat()))
                .andReturn();

        verify(productDao, times(1)).findById(2);
    }

    @Test
    public void afficherUnProduitShouldThrowProduitIntrouvableExceptionWhenAWrongIdIsGiven() throws Exception {
        this.mockMvc.perform(get("/Produits/{id}", 11))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ProduitIntrouvableException))
                .andExpect(result -> assertEquals("Le produit avec l'id 11 est INTROUVABLE. Écran Bleu si je pouvais.", result.getResolvedException().getMessage()))
                .andReturn();

        verify(productDao, times(1)).findById(11);
    }

    @Test
    public void getNoteProduitShouldCorrectlyReturnANote() throws Exception {
        String expectedNote = "Reconditionné par Apple, batterie remise à neuf.";

        when(productDao.findById(1)).thenReturn(products.get(0));
        when(productNotesDelegate.callNotesService(1)).thenReturn(expectedNote);
        when(restTemplate.getForEntity("http://localhost:8083/Notes/1", String.class)).thenReturn(new ResponseEntity<>(expectedNote, HttpStatus.OK));

        this.mockMvc.perform(get("/Produits/{id}/Note", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(result.getResponse().getContentAsString(), expectedNote))
                .andReturn();

        verify(productDao, times(1)).findById(1);
        verify(productNotesDelegate, times(1)).callNotesService(1);
    }

    @Test
    public void getNoteProduitShouldThrowProduitIntrouvableExceptionWhenAnUnknownProductIdIsGiven() throws Exception {
        this.mockMvc.perform(get("/Produits/{id}/Note", 11))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ProduitIntrouvableException))
                .andExpect(result -> assertEquals("Le produit avec l'id 11 est INTROUVABLE. Écran Bleu si je pouvais.", result.getResolvedException().getMessage()))
                .andReturn();

        verify(productDao, times(1)).findById(11);
        verify(productNotesDelegate, times(0)).callNotesService(11);
    }

    @Test
    public void ajouterProduitShouldCorrectlyAddANewProduct() throws Exception {
        Product newProduct = new Product(4, "iPhone 12 Pro", 1200, 1050);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(newProduct);

        when(productDao.save(newProduct)).thenReturn(newProduct);

        this.mockMvc.perform(post("/Produits").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
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

        verify(productDao, times(0)).save(newProduct);
        verify(productDao, times(0)).findById(4);
    }

    @Test
    public void supprimerProduitShouldCorrectlyDeleteAProduct() throws Exception {
        when(productDao.findById(1)).thenReturn(products.get(0));

        this.mockMvc.perform(delete("/Produits/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        verify(productDao, times(1)).delete(1);
    }

    @Test
    public void supprimerProduitShouldThrowProduitIntrouvableExceptionWhenAnUnknownProductIdIsGiven() throws Exception {
        this.mockMvc.perform(delete("/Produits/{id}", 11))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ProduitIntrouvableException))
                .andExpect(result -> assertEquals("Le produit avec l'id 11 est INTROUVABLE. Écran Bleu si je pouvais.", result.getResolvedException().getMessage()))
                .andReturn();

        verify(productDao, times(0)).delete(11);
    }

    @Test
    public void updateProduitShouldCorrectlyUpdateAProduct() throws Exception {
        Product updatedProduct = new Product(1, "Ordinateur portable", 300, 100);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(updatedProduct);

        when(productDao.save(updatedProduct)).thenReturn(updatedProduct);
        when(productDao.findById(1)).thenReturn(updatedProduct);

        this.mockMvc.perform(put("/Produits").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        verify(productDao, times(1)).findById(1);

        this.mockMvc.perform(get("/Produits/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedProduct.getId()))
                .andExpect(jsonPath("$.nom").value(updatedProduct.getNom()))
                .andExpect(jsonPath("$.prix").value(updatedProduct.getPrix()))
                .andExpect(jsonPath("$.prixAchat").value(updatedProduct.getPrixAchat()))
                .andReturn();
    }

    @Test
    public void updateProduitShouldThrowProduitIntrouvableExceptionWhenAnUnknownProductIdIsGiven() throws Exception {
        Product updatedProduct = new Product(11, "Ordinateur portable", 300, 100);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(updatedProduct);

        this.mockMvc.perform(put("/Produits").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ProduitIntrouvableException))
                .andExpect(result -> assertEquals("Le produit avec l'id 11 est INTROUVABLE. Écran Bleu si je pouvais.", result.getResolvedException().getMessage()))
                .andReturn();

        verify(productDao, times(0)).save(updatedProduct);
        verify(productDao, times(0)).findById(1);
    }

    @Test
    public void testeDeRequetesShouldCorrectlyReturnProductsUnderPrice400() throws Exception {
        when(productDao.chercherUnProduitCher(400)).thenReturn(Arrays.asList(products.get(1), products.get(2)));

        this.mockMvc.perform(get("/Test/Produits/{prix}", 400))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andReturn();
    }
}
