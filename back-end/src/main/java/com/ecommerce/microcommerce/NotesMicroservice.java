package com.ecommerce.microcommerce;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class NotesMicroservice {

  @GetMapping("/Notes/{idProduct}")
  public String readingList(@PathVariable int idProduct){
    return idProduct + "";
  }

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(NotesMicroservice.class);
    app.setDefaultProperties(Collections.singletonMap("server.port", "8083"));
    app.run(args);
  }
}
