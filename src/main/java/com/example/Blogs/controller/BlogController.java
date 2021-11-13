package com.example.Blogs.controller;

import com.example.Blogs.Exception.ApiRequestException;
import com.example.Blogs.entity.*;
import com.example.Blogs.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
public class BlogController {
    @Autowired
    private BlogService blogService;

    @GetMapping("/api/ping")
    public ResponseEntity<HashMap<String, Boolean>> getStatus() {
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("Success", true);
        return new ResponseEntity<HashMap<String, Boolean>>(map, HttpStatus.OK);
    }

    @GetMapping("/api/posts")
    public ResponseEntity<List<Post>> getResponseList(@RequestParam(value = "tags", required = true) List<String> tags,
                                                         @RequestParam(value = "sortBy", required = false) String sortBy,
                                                         @RequestParam(value = "direction", required = false) String direction) {

        List<String> acceptedInputsForSortBy = Arrays.asList("id", "reads", "likes", "popularity");
        List<String> acceptedInputsForDirection = Arrays.asList("desc", "asc");
        List<String> acceptedInputsForTags = Arrays.asList("tech", "health");
        HashSet<Post> returnList = new HashSet<>();

        if (sortBy!=null  && !acceptedInputsForSortBy.contains(sortBy)) {
            throw new ApiRequestException("sortBy parameter is invalid");
        } else if (direction!=null  && !acceptedInputsForDirection.contains(direction)){
            throw new ApiRequestException("direction parameter is invalid");
        } else if(tags!=null && !acceptedInputsForTags.containsAll(tags)){
            throw new ApiRequestException("Tag parameter is invalid");
        }

        List<Post> postsList = blogService.getFormatPosts(tags, sortBy, direction, returnList);

        return new ResponseEntity<>(postsList, HttpStatus.OK);
    }












}
