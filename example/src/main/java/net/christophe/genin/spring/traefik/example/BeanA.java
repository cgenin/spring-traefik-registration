package net.christophe.genin.spring.traefik.example;

public class BeanA {

    private String value;

    public BeanA(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
