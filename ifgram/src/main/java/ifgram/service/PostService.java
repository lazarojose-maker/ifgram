package ifgram.service;

import ifgram.Model.Post; // Ajustado para letra minúscula (padrão Java)
import ifgram.Model.User;
import ifgram.Repository.PostRepository;
import ifgram.Repository.UserRepository;
import ifgram.dto.PostRequest;
import ifgram.dto.PostResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
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
                .orElseThrow(() -> new RuntimeException("Autor não encontrado com o ID: " + request.autorId()));

        Post post = new Post();
        post.setImagemUrl(request.imagemUrl());
        post.setLegenda(request.legenda());
        post.setAutor(autor);

        Post postSalvo = postRepository.save(post);
        return PostResponse.from(postSalvo);
    }

    public List<PostResponse> listarFeed() {
        return postRepository.findAllByOrderByCriadoEmDesc()
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    public List<PostResponse> listarPorAutor(Long autorId) {
        return postRepository.findByAutor_IdOrderByCriadoEmDesc(autorId)
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    public PostResponse buscarPorId(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post não encontrado com o ID: " + id));
        return PostResponse.from(post);
    }

    @Transactional
    public void deletar(Long id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("Post não encontrado com o ID para remoção: " + id);
        }
        postRepository.deleteById(id);
    }
}