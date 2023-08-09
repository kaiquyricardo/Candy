package com.github.kaiquy.candy.data.candy;

import com.github.kaiquy.candy.misc.Cache;

public class CandyCache extends Cache<Candy> {

    public Candy getById(String candyId) {
        return getCached(candy -> candy.getId().equalsIgnoreCase(candyId));
    }
}
