package com.github.kaiquy.candy.data.insulin;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Insulin {

    private String id;
    private String name, texture;
    private int ml;

}
