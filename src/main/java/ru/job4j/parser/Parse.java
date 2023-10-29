package ru.job4j.parser;

import ru.job4j.model.Post;

import java.util.List;

/**
 * Парсинг сайта
 */
public interface Parse {

    /**
     * Возвращает распарсенный список всех постов в виде вакансий
     * @return распарсенный список всех постов
     */
    List<Post> list();

    /**
     * Возвращает распарсенный пост
     * @param link ссылка на ресурс сайта с детальным описанием поста на входе
     * @return распарсенный пост
     */
    Post detail(String link);
}
