package api.why.uz.api.why.uz.controller;

import api.why.uz.api.why.uz.dto.post.PostCreateDTO;
import api.why.uz.api.why.uz.dto.post.PostDTO;
import api.why.uz.api.why.uz.service.PostService;
import api.why.uz.api.why.uz.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create-post")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostCreateDTO dto){
      return ResponseEntity.ok(postService.create(dto));
    }

    @GetMapping("/post/profile")
    public ResponseEntity<PageImpl<PostDTO>> getPosts(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "9") int size){
        return ResponseEntity.ok(postService.getProfilePostList(PageUtil.page(page),size));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<PostDTO> getById(@PathVariable String id){
        return ResponseEntity.ok(postService.getById(id));
    }

    @PostMapping("/public-posts")
    public ResponseEntity<PageImpl<PostDTO>> getAll(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "size", defaultValue = "9") int size){
        return ResponseEntity.ok(postService.getAll(PageUtil.page(page), size));
    }

    @PutMapping("/update-post/{id}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable String id, @RequestBody PostCreateDTO dto){
        return ResponseEntity.ok(postService.updatePost(id, dto));
    }

    @PutMapping("/delete-post/{id}")
    public ResponseEntity<String> deletePost(@PathVariable String id){
        return ResponseEntity.ok(postService.deletePost(id));
    }
}
