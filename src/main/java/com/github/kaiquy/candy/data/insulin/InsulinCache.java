package com.github.kaiquy.candy.data.insulin;

import com.github.kaiquy.candy.misc.Cache;

public class InsulinCache extends Cache<Insulin> {

    public Insulin getById(String candyId) {
        return getCached(insulin -> insulin.getId().equalsIgnoreCase(candyId));
    }
}
