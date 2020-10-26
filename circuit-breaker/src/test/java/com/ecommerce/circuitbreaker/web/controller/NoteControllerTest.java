package com.ecommerce.circuitbreaker.web.controller;

import com.ecommerce.circuitbreaker.CircuitBreakerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static junit.framework.TestCase.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CircuitBreakerApplication.class)
@WebMvcTest(controllers = NoteController.class)
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getNoteShouldReturnTheCorrectMessageForAGivenId() throws Exception {
        this.mockMvc.perform(get("/Notes/{idProduct}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(result.getResponse().getContentAsString(), "Reconditionné par Apple, batterie remise à neuf."))
                .andReturn();
    }

    @Test
    public void getNoteShouldReturnASpecificMessageWhenAWrongProductIdIsGiven() throws Exception {
        this.mockMvc.perform(get("/Notes/{idProduct}", 11))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(result.getResponse().getContentAsString(), "Aucune note pour ce produit"))
                .andReturn();
    }
}
