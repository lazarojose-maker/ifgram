package ifgram.Repository;

import ifgram.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

    public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCriadoEmDesc();

    // Note o _ (underline) aqui:
    List<Post> findByAutor_IdOrderByCriadoEmDesc(Long autorId);
}