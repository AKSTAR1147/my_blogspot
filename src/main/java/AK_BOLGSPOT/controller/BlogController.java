package AK_BOLGSPOT.controller;

import AK_BOLGSPOT.model.BlogPost;
import AK_BOLGSPOT.model.Comment;
import AK_BOLGSPOT.model.Like;
import AK_BOLGSPOT.repository.BlogRepository;
import AK_BOLGSPOT.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

        return "redirect:/post/{id}";  // refresh homepage to show updated like count
    }


    @GetMapping("/showBlogForm")
    public String blogForm(Model model){

        //create modal attribute to bind form data
        BlogPost post = new BlogPost();

        //add attribute to model
        model.addAttribute("newPost",post);

        return "addPost";
    }

    @GetMapping("/post/{id}")
    public String showPost(@PathVariable Long id, Model model) {

        //searching for the post in the DB and saving in the new object
        BlogPost post = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        //adding the found post to the model for serving to the html page
        model.addAttribute("post",post);
        model.addAttribute("newComment", new Comment());

        //takes you to the page showing the only post you clicked on
        return "showPost";
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("newPost") BlogPost blogPost){

        //save the employee
        blogPost.setCreatedAt(LocalDate.now() );
        blogRepository.save(blogPost);

        //use a redirect to prevent duplicate submissions
        return "redirect:/";
    }

//    @GetMapping("/post/{id}")
//    public String post(@PathVariable Long id, Model model) {
//        BlogPost post = blogRepository.findById(id);
//        model.addAttribute("post", post);
//        return "post";
//    }
//
//    @PostMapping("/post/{id}/comment")
//    public String addComment(@PathVariable Long id, @RequestParam String content) {
//        Comment comment = new Comment();
//        comment.setContent(content);
//        comment.setAuthor("Anonymous"); // You can modify this to get the actual user
//        blogRepository.addComment(id, comment);
//        return "redirect:/post/" + id;
//    }
}

