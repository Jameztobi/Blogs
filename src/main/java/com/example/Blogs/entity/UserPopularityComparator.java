package com.example.Blogs.entity;

import java.util.Comparator;

public class UserPopularityComparator implements Comparator<Post> {
    @Override
    public int compare(Post post1, Post post2) {
        return (int) (post1.getPopularity()-post2.getPopularity());
    }
}
