package AK_BOLGSPOT.repository;


import AK_BOLGSPOT.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<BlogPost,Long> {

}
