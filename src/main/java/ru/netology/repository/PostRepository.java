package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
// Stub
public class PostRepository {

    private AtomicLong globalId = new AtomicLong(0);
    private ConcurrentHashMap<Long, Post> hashMap = new ConcurrentHashMap<>();

    public List<Post> all() {
        return hashMap.values().stream()
                .filter(post -> !post.isRemoved())
                .collect(Collectors.toList());
    }

    public Optional<Post> getById(long id) {
        if (hashMap.containsKey(id))
        {
            var post = hashMap.get(id);
            if (!post.isRemoved())
            {
                return Optional.of(post);
            }
        }
        return Optional.empty();
    }

    public Post save(Post post) {
        var id = post.getId();
        if (id == 0) {
            var newId = globalId.getAndIncrement();
            post.setId(newId);
            hashMap.put(newId, post);
        }
        if (hashMap.containsKey(id) && !hashMap.get(id).isRemoved()) {
            hashMap.put(post.getId(), post);
        } else throw new NotFoundException("Id " + post.getId() + " was not found");
        return post;
    }

    public void removeById(long id) {
        if (hashMap.get(id) == null)
            throw new NotFoundException("Id " + id + " was not found");
        else hashMap.get(id).setRemoved(true);
    }
}
