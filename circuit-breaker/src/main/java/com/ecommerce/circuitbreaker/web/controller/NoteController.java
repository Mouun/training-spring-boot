package com.ecommerce.circuitbreaker.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoteController {

    @GetMapping("/Notes/{idProduct}")
    public String getNote(@PathVariable int idProduct) {
        switch (idProduct) {
            case 1:
                return "Reconditionné par Apple, batterie remise à neuf.";
            case 2:
                return "Reconditionné.";
            case 3:
                return "Reconditionné par Décathlon, lot de 2 raquettes offert.";
            default:
                return "Aucune note pour ce produit";
        }
    }
}
