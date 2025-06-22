package ru.practicum.project.model;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

	private Long id;
    private String title;
    private String text;
    private String imagePath;
    private int likesCount;
    private List<Comment> comments;
    private List<String> tags;
    
    public Post(Long id, String title, String text, String imagePath, int likesCount) {
    	this.id = id;
    	this.title = title;
    	this.text = text;
    	this.imagePath = imagePath;
    	this.likesCount = likesCount;
    	this.comments = List.of();
    	this.tags = List.of();
    }
    
    public String getTagsAsText() {
        return String.join(", ", tags);
    }
    
    public List<String> getTextParts() {
        return text == null ? List.of() : Arrays.asList(text.split("\\n"));
    }
    
    public String getTextPreview() {
        if (text == null) return "";
        return text.length() > 150 ? text.substring(0, 150) + "..." : text;
    }
}


