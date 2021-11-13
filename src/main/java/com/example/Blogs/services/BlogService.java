package com.example.Blogs.services;

import com.example.Blogs.BlogsApplication;
import com.example.Blogs.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlogService {
    private static final Cache cache = new Cache();
    private static final Logger log = LoggerFactory.getLogger(BlogsApplication.class);
    @Autowired
    RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    public List<Post> getFormatPosts(List<String> tags, String sortBy, String direction, HashSet<Post> returnList) {
        retrievePost(tags, returnList);
        List<Post> postsList = new ArrayList<>();

        sortBy = sortBy==null ? "id": sortBy;
        if(direction!=null && direction.equals("desc")){
            postsList  = getPosts(sortBy, returnList, direction);
        }
        else{
            postsList  = getPosts(sortBy, returnList, "asc");
        }
        return postsList;
    }

    private void retrievePost(List<String> tags, HashSet<Post> returnList) {
        String[] value = tags.toArray(new String[0]);

        for (String str : value) {
            if (!str.isEmpty()) {
                try {
                    returnList.addAll(insertPost(getBlog(str)));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private HashSet<Post> insertPost(Post[] posts) {
        HashSet<Post> returnList = new HashSet<>();


        for (Post post: posts) {
            returnList.add(post);
        }
        return returnList;
    }

    private Post[] getBlog(String str) throws MalformedURLException {
        log.info(str, (Object) cache.get(str));
        if(cache.get(str)!=null){
            log.info("Retrieve from Cache");
            return cache.get(str);
        }
        log.info("Retrieve from API");
        Pojo pojo = restTemplate.getForObject(new URL("https://api.hatchways.io/assessment/blog/posts?tag=" + str).toString(), Pojo.class);
        cache.set(str, pojo.getPosts());
        return pojo.getPosts();
    }

    private List<Post> getPosts(String sortBy, HashSet<Post> returnList, String direction) {
        List<Post> postsList = new ArrayList<>();
        if(sortBy != null &&  direction.equals("desc")){
            postsList = format((ArrayList<Post>) returnList.stream().collect(Collectors.toList()), sortBy, direction);
        }
        else if(sortBy != null && direction.equals("asc")){
            postsList = format((ArrayList<Post>) returnList.stream().collect(Collectors.toList()), sortBy, direction);
        }
        else{
            postsList = (ArrayList<Post>) returnList.stream().collect(Collectors.toList());
        }
        return postsList;
    }



    public List<Post> format(List<Post> posts, Object value, String direction){

        if(value.equals("id")){
            Collections.sort(posts, new UserIdComparator());
            if(direction.equals("desc")){
                Collections.sort(posts, Comparator.comparingInt(Post::getId).reversed());
            }
        }
        else if(value.equals("likes")){
            Collections.sort(posts, new UserLikesComparator());
            if(direction.equals("desc")){
                Collections.sort(posts, Comparator.comparingInt(Post::getLikes).reversed());
            }
        }
        else if(value.equals("popularity")){
            Collections.sort(posts, new UserPopularityComparator());
            if(direction.equals("desc")){
                Collections.sort(posts, Comparator.comparingInt(Post::getPopularity).reversed());
            }
        }
        else if(value.equals("reads")){
            Collections.sort(posts, new UserReadComparator());
            if(direction.equals("desc")){
                Collections.sort(posts, Comparator.comparingInt(Post::getReads).reversed());
            }
        }

        return posts;
    }

}
