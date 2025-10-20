package AK_BOLGSPOT.model;


import jakarta.persistence.*;

@Entity
@Table(name="likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blogpost_id")
    private BlogPost blogPost;

    // Constructors, getters and setters

    public Like() {
    }

    public Like(BlogPost blogPost) {
        this.blogPost = blogPost;
    }

    //getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BlogPost getBlogPost() {
        return blogPost;
    }

    public void setBlogPost(BlogPost blogPost) {
        this.blogPost = blogPost;
    }
}
