package com.kennel.backend.repository;

import com.kennel.backend.entity.Post;
import com.kennel.backend.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Test
    void shouldFindPostsByUserWithPagination() {
        Post p1 = Post.builder().content("Post 1").slug("slug-1").createdBy(user).build();
        Post p2 = Post.builder().content("Post 2").slug("slug-2").createdBy(user).build();
        entityManager.persist(p1);
        entityManager.persist(p2);

        Pageable pageable = PageRequest.of(0, 1, Sort.by("createdAt").descending());
        Page<Post> page = postRepository.findByCreatedBy(user, pageable);

        assertEquals(1, page.getContent().size());
        assertEquals("slug-2", page.getContent().get(0).getSlug()); // Based on createdAt order
    }
    @Test
    void shouldFindPostsByUserWithPaginationNextPage() {
        Post p1 = Post.builder().content("Post 1").slug("slug-1").createdBy(user).build();
        Post p2 = Post.builder().content("Post 2").slug("slug-2").createdBy(user).build();
        entityManager.persist(p1);
        entityManager.persist(p2);

        Pageable pageable = PageRequest.of(1, 1, Sort.by("createdAt").descending());
        Page<Post> page = postRepository.findByCreatedBy(user, pageable);

        assertEquals(1, page.getContent().size());
        assertEquals("slug-1", page.getContent().get(0).getSlug()); // Based on createdAt order
    }

}
