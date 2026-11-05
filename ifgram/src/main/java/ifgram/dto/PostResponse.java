package ifgram.dto;

import ifgram.Model.Post;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String imagemUrl,
        String legenda,
        LocalDateTime criadoEm,
        UserResponse autor
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getImagemUrl(),
                post.getLegenda(),
                post.getCriadoEm(),
                UserResponse.from(post.getAutor())
        );
    }
}