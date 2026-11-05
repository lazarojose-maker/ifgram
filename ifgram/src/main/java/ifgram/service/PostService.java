package ifgram.service;

import ifgram.Model.Post;
import ifgram.Model.User;
import ifgram.Repository.PostRepository;
import ifgram.Repository.UserRepository;
import ifgram.dto.PostRequest;
import ifgram.dto.PostResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PostResponse criar(PostRequest request) {
        User autor = userRepository.findById(request.autorId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + request.autorId()));

        Post post = new Post(request.imagemUrl(), request.legenda(), autor);
        Post postSalvo = postRepository.save(post);
        return PostResponse.from(postSalvo);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getFeed() {
        return postRepository.findFeed()
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PostResponse buscarPorId(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post não encontrado com ID: " + id));
        return PostResponse.from(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> buscarPorAutor(Long autorId) {
        return postRepository.findByAutorId(autorId)
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    @Transactional
    public void deletar(Long id) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("Post não encontrado com ID: " + id);
        }
        postRepository.deleteById(id);
    }
}