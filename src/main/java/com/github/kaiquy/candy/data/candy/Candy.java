package com.github.kaiquy.candy.data.candy;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Candy {

    private String id;
    private String name, texture;
    private int sugar, food;

}
