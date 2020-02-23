package net.christophe.genin.spring.traefik.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RestServiceTest {

    @GetMapping("/my-resource")
    public BeanA getResource(){
        return new BeanA("Test");
    }
}
