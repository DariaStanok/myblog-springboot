package ru.practicum.project.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.ServletContext;
import ru.practicum.project.configuration.MockedServicesConfiguration;
import ru.practicum.project.model.Paging;
import ru.practicum.project.model.Post;
import ru.practicum.project.service.CommentService;
import ru.practicum.project.service.PostService;


@WebMvcTest(controllers = { PostController.class, HomeController.class })
@Import(MockedServicesConfiguration.class)


class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
    private ServletContext сontext;
	@Autowired
	private PostService postService;
	@Autowired
	private CommentService commentService;
	private Post testPost;

	@BeforeEach
	void setup() {
		
	
		testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("Test Post");
        testPost.setComments(new ArrayList<>());
        testPost.setTags(new ArrayList<>());
	}

	 @Test
	    void servletContextAvailable() {
	        assertNotNull(сontext);
	        assertTrue(сontext instanceof MockServletContext);

	}
	@Test
	void postsPageTest() throws Exception { 
	    when(postService.findAll(eq(""), any(Paging.class))).thenReturn(List.of(testPost));
	    mockMvc.perform(get("/posts"))
	        .andExpect(status().isOk())
	        .andExpect(view().name("posts"))
	        .andExpect(model().attributeExists("posts"))
	        .andExpect(model().attributeExists("search"))
	        .andExpect(model().attributeExists("pageSize"))
	        .andExpect(model().attributeExists("pageNumber"))
	        .andExpect(model().attributeExists("hasPrevious"))
	        .andExpect(model().attributeExists("hasNext"));
	}

	@Test
	void homePagePostsTest() throws Exception { 
	    mockMvc.perform(get("/"))
	           .andExpect(status().is3xxRedirection())
	           .andExpect(redirectedUrl("/posts"));
	}
	
	@Test
	void postPageTest() throws Exception {
		when(postService.findById(1L)).thenReturn(testPost);  
	    mockMvc.perform(get("/posts/1"))
	        .andExpect(status().isOk())
	        .andExpect(view().name("post"))
	        .andExpect(model().attributeExists("post"))
	        .andExpect(model().attribute("post", testPost));
	}
	
	@Test
	void addPostPageTest() throws Exception { 
	    mockMvc.perform(get("/posts/add"))
	           .andExpect(status().isOk())
	           .andExpect(view().name("add-post"))
	           .andExpect(model().attributeExists("post")) 
	           .andExpect(model().attribute("post", Matchers.<Post>hasProperty(
	                   "comments", Matchers.empty())))
	           .andExpect(model().attribute("post", Matchers.<Post>hasProperty(
	                   "tags", Matchers.empty())));
	}

	@Test
	void createPostPageTest() throws Exception {
		MockMultipartFile image = new MockMultipartFile("image", "photo.jpg", "image/jpeg", new byte[0]);
		Post saved = new Post();
		saved.setId(3L);
		when(postService.save(any(Post.class), eq(image), eq("tag1"))).thenReturn(saved);
		mockMvc.perform(multipart("/posts")
		   .file(image).param("title", "title text")
		   .param("content", "content text")
		   .param("tags", "tag1"))
	       .andExpect(status().is3xxRedirection())
	       .andExpect(redirectedUrl("/posts/3"));
	}
	
	@Test
	void postsPagePaginationTest() throws Exception {
	    when(postService.findAll(eq("tags"), any(Paging.class))).thenReturn(List.of(testPost)); 
	    mockMvc.perform(get("/posts")
	            .param("search", "tags") 
	            .param("pageSize", "5")
	            .param("pageNumber", "2"))
	        .andExpect(status().isOk())
	        .andExpect(view().name("posts"))
	        .andExpect(model().attributeExists("posts"))
	        .andExpect(model().attribute("search", "tags"))
	        .andExpect(model().attribute("pageSize", 5))
	        .andExpect(model().attribute("pageNumber", 2));
	}
	
	@Test
	void addCommentPageTest() throws Exception { 
	    mockMvc.perform(post("/posts/1/comments")
	        .param("text", "like!")) 
	        .andExpect(status().is3xxRedirection())
	        .andExpect(redirectedUrl("/posts/1"));
	    verify(commentService).addComment(1L, "like!");
	}
	
	@Test
	void editCommentPageTest() throws Exception {
	    mockMvc.perform(post("/posts/1/comments/10").param("text", "Updated comment"))
	        .andExpect(status().is3xxRedirection())
	        .andExpect(redirectedUrl("/posts/1"));
	    verify(commentService).editComment(1L, 10L, "Updated comment");
	}
	
	@Test
	void editPostPageTest() throws Exception {
	    when(postService.findById(1L)).thenReturn(testPost);
	    mockMvc.perform(get("/posts/1/edit"))
	        .andExpect(status().isOk())
	        .andExpect(view().name("add-post"))
	        .andExpect(model().attributeExists("post"))
	        .andExpect(model().attribute("post", testPost));
	}
	
	@Test
	void deleteCommentTest() throws Exception {
	    mockMvc.perform(post("/posts/1/comments/10/delete"))
	        .andExpect(status().is3xxRedirection())
	        .andExpect(redirectedUrl("/posts/1"));
	    verify(commentService).deleteComment(1L, 10L);
	}
	
	@Test
	void updatePostPageTest() throws Exception {
	    MockMultipartFile image = new MockMultipartFile("image", "photo.jpg", "image/jpeg", new byte[0]);
	    mockMvc.perform(multipart("/posts/1")
	            .file(image)
	            .param("title", "Updated")
	            .param("content", "Updated content")
	            .param("tags", "tag2")
	            .with(request -> { request.setMethod("POST"); return request; }))
           .andExpect(status().is3xxRedirection()) 
           .andExpect(redirectedUrl("/posts/1")); 
	    verify(postService).update(eq(1L), any(Post.class), eq(image), eq("tag2"));
	}
	
	@Test
	void deletePostPageTest() throws Exception {
	    mockMvc.perform(post("/posts/1/delete"))
	        .andExpect(status().is3xxRedirection())
	        .andExpect(redirectedUrl("/posts"));
	    verify(postService).deleteById(1L);
	}
	
	@Test
	void likePostPageTest() throws Exception {
	    mockMvc.perform(post("/posts/1/like").param("like", "true"))
	        .andExpect(status().is3xxRedirection())
	        .andExpect(redirectedUrl("/posts/1"));
	    verify(postService).like(1L, true);
	}
	
	
}
