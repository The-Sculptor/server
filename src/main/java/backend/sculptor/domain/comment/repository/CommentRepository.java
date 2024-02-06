package backend.sculptor.domain.comment.repository;

import backend.sculptor.domain.comment.entity.Comment;
import backend.sculptor.domain.stone.entity.Stone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByStoneId(UUID stoneId);
}
