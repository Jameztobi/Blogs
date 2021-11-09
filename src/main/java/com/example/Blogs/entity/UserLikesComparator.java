package com.example.Blogs.entity;

import java.util.Comparator;

public class UserLikesComparator implements Comparator<Post> {
    @Override
    public int compare(Post o1, Post o2) {
        return o1.getLikes()-o2.getLikes();
    }
}
