package api.why.uz.api.why.uz.controller;

import api.why.uz.api.why.uz.util.SpringSecurityUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class PostController {

    @PostMapping("/create-post")
    public String createPost(){
        System.out.println(SpringSecurityUtil.getCurrentProfile());;
        return "createPost";
    }
}
