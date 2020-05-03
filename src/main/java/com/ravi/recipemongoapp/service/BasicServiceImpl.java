package com.ravi.recipemongoapp.service;

import org.springframework.stereotype.Service;

@Service
public class BasicServiceImpl implements BasicService {

    @Override
    public String getRecipe() {
        return "Basic Recipe";
    }
}
