package com.example.Blogs.entity;

import java.util.HashMap;

public class Cache {

    private HashMap<String, Post[]> cache = new HashMap<>();
    public void set(String key, Post[] posts){
        cache.put(key, posts);
    }

    public Post[] get(String key){
        if(cache.containsKey(key)){
            return cache.get(key);
        }
        return null;
    }
}
