package AK_BOLGSPOT.controller;

import AK_BOLGSPOT.model.BlogPost;
import AK_BOLGSPOT.model.Comment;
import AK_BOLGSPOT.model.Like;
import AK_BOLGSPOT.repository.BlogRepository;
import AK_BOLGSPOT.repository.CommentRepository;
import AK_BOLGSPOT.repository.LikeRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
//@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<BlogPost> posts = blogRepository.findAllByOrderByCreatedAtDesc();
        model.addAttribute("posts", posts);
        return "index";
    }

    // Like a post
    @PostMapping("/post/{id}/like")
    public String likePost(@PathVariable Long id) {
        BlogPost post = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Like like = new Like();
        like.setBlogPost(post);

        likeRepository.save(like);

        return "redirect:/post/" + id; // refresh homepage to show updated like count
    }

    @PostMapping("/post/{id}/comment")
    public String commentPost(@PathVariable Long id,
                              @RequestParam  String content,
                              @AuthenticationPrincipal OidcUser principal){


        BlogPost post = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // 2. Get the author's name directly from the Google user's profile
        String author = principal.getAttribute("name");

        Comment comment = new Comment(content,author,post);
        commentRepository.save(comment);


        return "redirect:/post/"+ id;
    }

    @GetMapping("/showBlogForm")
    public String showGetStarted(){
        return "getStarted";
    }

    @GetMapping("/loginDB")
    public String shoLoginForm(){
        return "login";
    }

    @GetMapping("/addPost")
    public String afterLoginAddPost(Model model){
        //create modal attribute to bind form data
        BlogPost post = new BlogPost();
        //add attribute to model
        model.addAttribute("newPost",post);

        return "addPost";
    }


    @GetMapping("/post/{id}")
    public String showPost(@PathVariable Long id, Model model, HttpServletRequest request) { // <-- Add HttpServletRequest here
        //searching for the post in the DB and saving in the new object
        BlogPost post = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        //adding the found post to the model for serving to the html page
        model.addAttribute("post", post);
        model.addAttribute("newComment", new Comment());

        // THIS IS THE NEW LINE: Add the current URL to the model
        model.addAttribute("currentUrl", request.getRequestURI());
        //takes you to the page showing the only post you clicked on
        return "showPost";
    }

    @PostMapping("/save")
    public String savePost(@Valid @ModelAttribute("newPost") BlogPost blogPost){

        //save the post

        blogRepository.save(blogPost);

        //use a redirect to prevent duplicate submissions
        return "redirect:/";
    }

    @GetMapping("/access-denied")
    public String showAccessDeniedPage() {
        return "access-denied"; // This will look for access-denied.html
    }
}

