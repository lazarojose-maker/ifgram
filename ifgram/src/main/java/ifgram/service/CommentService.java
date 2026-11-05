package ifgram.service;

import ifgram.Model.Comment;
import ifgram.Model.Post;
import ifgram.Model.User;
import ifgram.Repository.CommentRepository;
import ifgram.Repository.PostRepository;
import ifgram.Repository.UserRepository;
import ifgram.dto.CommentRequest;
import ifgram.dto.CommentResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public CommentResponse criar(CommentRequest request) {
        User autor = userRepository.findById(request.autorId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + request.autorId()));

        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new IllegalArgumentException("Post não encontrado com ID: " + request.postId()));

        Comment comment = new Comment(request.texto(), post, autor);
        Comment commentSalvo = commentRepository.save(comment);
        return CommentResponse.from(commentSalvo);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> listarPorPost(Long postId) {
        return commentRepository.findByPostIdOrderByCriadoEmAsc(postId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    @Transactional
    public void deletar(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new IllegalArgumentException("Comentário não encontrado com ID: " + id);
        }
        commentRepository.deleteById(id);
    }
}