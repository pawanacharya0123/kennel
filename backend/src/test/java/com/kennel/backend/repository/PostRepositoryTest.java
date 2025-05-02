package com.kennel.backend.repository;

import com.kennel.backend.entity.Post;
import com.kennel.backend.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TestEntityManager entityManager;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword("secret-password");
        entityManager.persist(user);
    }

    @Test
    void shouldFindPostBySlug() {
        Post post = Post.builder()
                .slug("test-slug")
                .content("Some post content")
                .createdBy(user)
                .build();

        entityManager.persist(post);

        Optional<Post> found = postRepository.findBySlug("test-slug");

        assertTrue(found.isPresent());
        assertEquals("test-slug", found.get().getSlug());
    }

    @Test
    void shouldDeletePostBySlug() {
        Post post = Post.builder()
                .slug("delete-me")
                .content("To be deleted")
                .createdBy(user)
                .build();

        entityManager.persist(post);
        postRepository.deleteBySlug("delete-me");

        assertFalse(postRepository.findBySlug("delete-me").isPresent());
    }

    @Test
    void shouldFindPostsByUserId() {
        Post post1 = Post.builder().slug("p1").content("1").createdBy(user).build();
        Post post2 = Post.builder().slug("p2").content("2").createdBy(user).build();

        entityManager.persist(post1);
        entityManager.persist(post2);

        List<Post> posts = postRepository.findByCreatedById(user.getId());

        assertEquals(2, posts.size());
    }

    @Test
    void shouldReturnTrueIfSlugExists() {
        Post post = Post.builder().slug("unique-slug").content("exists").createdBy(user).build();
        entityManager.persist(post);

        assertTrue(postRepository.existsBySlug("unique-slug"));
    }

    @Test
    void shouldFindPostsByUserEntity() {
        Post post = Post.builder().slug("user-post").content("test").createdBy(user).build();
        entityManager.persist(post);

        List<Post> posts = postRepository.findByCreatedBy(user);

        assertEquals(1, posts.size());
        assertEquals("user-post", posts.get(0).getSlug());
    }

}
