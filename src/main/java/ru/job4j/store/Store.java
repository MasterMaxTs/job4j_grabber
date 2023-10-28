package ru.job4j.store;

import ru.job4j.model.Post;

import java.util.List;

/**
 * Хранилище постов
 */
public interface Store {

    /**
     * Сохраняет пост в хранилище
     * @param post пост
     */
    void save(Post post);

    /**
     * Возвращает список постов из хранилища
     * @return список постов
     */
    List<Post> getAll();
}
