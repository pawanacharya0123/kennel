package com.kennel.backend.utility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class SlugGenerator {
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public String toSlug(String input){
        String noWhiteSpace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noWhiteSpace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");

        return slug.toLowerCase(Locale.ENGLISH);
    }
    public String generateSlugWithUUID(String input) {
        return toSlug(input) + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public String ensureUniqueSlug(String baseInput, JpaRepository<?, ?> repository) {
        String baseSlug = toSlug(baseInput);
        String slug = baseSlug;
        int counter = 1;

        while (repositoryExistsBySlug(repository, slug)) {
            slug = baseSlug + "-" + counter++;
        }

        return slug;
    }

    private boolean repositoryExistsBySlug(JpaRepository<?, ?> repository, String slug) {
        try {
            return (boolean) repository.getClass()
                    .getMethod("existsBySlug", String.class)
                    .invoke(repository, slug);
        } catch (Exception e) {
            throw new IllegalArgumentException("Repository must have a method existsBySlug(String slug)", e);
        }
    }
}
