package ru.practicum.project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ru.practicum.project.model.Paging;
import ru.practicum.project.model.Post;
import ru.practicum.project.service.CommentService;
import ru.practicum.project.service.PostService;

@Controller
@RequestMapping("/posts")
public class PostController {

	private final PostService service;
	private final CommentService commentService;

    public PostController(PostService service, CommentService commentService) {
        this.service = service;
        this.commentService = commentService; 
    }
    
    @GetMapping
    public String showPostsPage(
    	     @RequestParam(name = "search",   defaultValue = "") String search,
    	     @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
    	     @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
    	     @RequestParam(name = "action", required = false) String action,
            Model model){ 
    	
    	 if ("next".equals(action)) {
    	        pageNumber++;
    	    } else if ("prev".equals(action) && pageNumber > 1) {
    	        pageNumber--;
    	    }
    	Paging paging = new Paging(pageSize, pageNumber, false); 
    	List<Post> paginatedPosts = service.findAll(search, paging);
    	model.addAttribute("posts", paginatedPosts);
    	model.addAttribute("search", search);
    	model.addAttribute("pageSize", paging.getPageSize());
    	model.addAttribute("pageNumber", paging.getPageNumber());
    	model.addAttribute("hasPrevious", paging.hasPrevious());
    	model.addAttribute("hasNext", paging.isHasNext()); 

        return "posts";
    }

    @GetMapping("/{id}")
    public String showPostPage(@PathVariable(name = "id")  Long id, Model model) {
        model.addAttribute("post", service.findById(id));
        return "post";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
    	Post post = new Post();
        post.setTags(List.of());      
        post.setComments(List.of());  
        model.addAttribute("post", post);
        return "add-post";
    }

    @PostMapping
    public String addPost(
            @ModelAttribute Post post,
            @RequestParam(name = "image") MultipartFile image,
    	    @RequestParam(name = "tags") String tags){
        Post saved = service.save(post, image,tags);
        return "redirect:/posts/" + saved.getId();
    }

    @GetMapping("/{id}/edit")
    public String editPostPage(@PathVariable(name = "id") Long id, Model model) {
        model.addAttribute("post", service.findById(id));
        return "add-post";
    }

    @PostMapping("/{id}")
    public String editPost(
            @PathVariable(name = "id") Long id,
            @ModelAttribute Post post,
            @RequestParam(name = "image",required = false) MultipartFile image,
            @RequestParam(name = "tags") String tags)  {
        service.update(id, post, image,tags);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable(name = "id")  Long id)  {
        service.deleteById(id);
        return "redirect:/posts";
    }

    @PostMapping("/{id}/like")
    public String like(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "like")boolean like) {
        service.like(id, like);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/comments")
    public String addComment(
            @PathVariable(name = "id")Long id,
            @RequestParam(name = "text") String text) {
        commentService.addComment(id, text);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/comments/{commentId}")
    public String editComment(
            @PathVariable(name = "id") Long id,
            @PathVariable(name = "commentId")Long commentId,
            @RequestParam (name = "text") String text) {
        commentService.editComment(id, commentId, text);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/comments/{commentId}/delete")
    public String deleteComment(
            @PathVariable(name = "id") Long id,
            @PathVariable(name = "commentId") Long commentId) {
        commentService.deleteComment(id, commentId);
        return "redirect:/posts/" + id;
    }
}
