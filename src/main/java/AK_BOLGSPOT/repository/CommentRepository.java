package AK_BOLGSPOT.repository;

import AK_BOLGSPOT.model.Comment;
import AK_BOLGSPOT.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}


