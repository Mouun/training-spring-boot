package com.ecommerce.microcommerce.web.delegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class ProductNotesDelegate {

  @Autowired RestTemplate restTemplate;

  @HystrixCommand(fallbackMethod = "callNotesFallback")
  public String callNotesService(int idProduit) {
    return restTemplate.exchange("http://localhost:8083/Notes/{idProduit}", HttpMethod.GET, null,
        new ParameterizedTypeReference<String>() {
        }, idProduit).getBody();
  }

  public String callNotesFallback(int idProduit) {
    return "Une erreur est survenue lors de la récuparation de la note de ce produit.";
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
