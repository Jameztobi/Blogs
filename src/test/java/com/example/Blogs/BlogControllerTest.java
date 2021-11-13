package com.example.Blogs;


import com.example.Blogs.Exception.ApiRequestException;
import com.example.Blogs.controller.BlogController;
import com.example.Blogs.services.BlogService;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BlogControllerTest {

    private BlogController blogController;
    private List<String> tags;
    private BlogService blogService = mock(BlogService.class);

    @Rule
    public ExpectedException exceptions = ExpectedException.none();

    @BeforeEach
    public void beforeEach() {
        this.blogController = new BlogController();
        tags = new ArrayList<>();
        TestUtil.InjectObjects(this.blogController, "blogService", blogService);
    }



    @Test
    public void testPing(){
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("Success", true);
        assertThat(blogController.getStatus().getBody()).isEqualTo(map);
    }

    @Test
    public void testInvalidTag(){
        tags.add("film");
        Exception exception = assertThrows(ApiRequestException.class, () ->
                blogController.getResponseList(tags, null, null));
        assertEquals("Tag parameter is invalid", exception.getMessage());

    }

    @Test
    public void testInvalidSortByInput(){
        tags.add("tech");
        Exception exception = assertThrows(ApiRequestException.class, () ->
                blogController.getResponseList(tags, "ids", null));
        assertEquals("sortBy parameter is invalid", exception.getMessage());

    }

    @Test
    public void testInvalidDirectionByInput(){
        tags.add("health");
        Exception exception = assertThrows(ApiRequestException.class, () ->
                blogController.getResponseList(tags, "id", "descs"));
        assertEquals("direction parameter is invalid", exception.getMessage());
    }

    @Test
    public void testValidTagsResponse(){
        tags.add("health");
        tags.add("tech");

        when(blogService.getFormatPosts(tags, null, null, null)).thenCallRealMethod();
        assertThat(blogController.getResponseList(tags, null, null).getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void testValidSortByResponse(){
        tags.add("health");
        tags.add("tech");

        List<String> sortBy = List.of("likes", "id", "popularity", "reads");
        for (String sort: sortBy) {
            when(blogService.getFormatPosts(tags, sort, null, null)).thenCallRealMethod();
            assertThat(blogController.getResponseList(tags, sort, null).getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        }
    }

    @Test
    public void testValidDirectionResponse(){
        tags.add("health");
        tags.add("tech");

        List<String> directions = List.of("asc", "desc");
        for (String direction: directions) {
            when(blogService.getFormatPosts(tags, null, direction, null)).thenCallRealMethod();
            assertThat(blogController.getResponseList(tags, null, direction).getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        }

    }


}
