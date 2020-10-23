package com.ecommerce.microcommerce;

import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.controller.ProductController;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MicrocommerceApplication.class)
@WebMvcTest(controllers = ProductController.class)
public class MicrocommerceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductController productController;

    private MappingJacksonValue produitsFiltres;

    private List<Product> products;

    @Before
    public void setUp() {
        products = new ArrayList<>();
        products.add(new Product(1, "Ordinateur portable", 350, 120));
        products.add(new Product(2, "Aspirateur Robot", 500, 200));
        products.add(new Product(3, "Table de Ping Pong", 750, 400));

        produitsFiltres = new MappingJacksonValue(products);
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
        this.produitsFiltres.setFilters(listDeNosFiltres);
    }

    @Test
    public void produitList() throws Exception {

        given(productController.listeProduits()).willReturn(produitsFiltres);

        this.mockMvc.perform(get("/Produits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0].nom").value(products.get(0).getNom()))
                .andExpect(jsonPath("$[0].prix").value(products.get(0).getPrix()))
                .andExpect(jsonPath("$[0].prixAchat").value(products.get(0).getPrixAchat()));
    }

    @Test
    public void listeProduitsShould() throws Exception {

        given(productController.listeProduits()).willReturn(produitsFiltres);

        this.mockMvc.perform(get("/Produits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0].nom").value(products.get(0).getNom()))
                .andExpect(jsonPath("$[0].prix").value(products.get(0).getPrix()))
                .andExpect(jsonPath("$[0].prixAchat").value(products.get(0).getPrixAchat()));
    }
}
