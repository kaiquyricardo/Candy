package com.github.kaiquy.candy.data.user;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    private final String name;
    private int diabetes;

    public void add(int value) {
        this.diabetes += value;
    }

    public void remove(int value) {
        this.diabetes -= value;
    }
}
