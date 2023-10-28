package ru.job4j.parser;

import ru.job4j.model.Post;

import java.util.List;

public interface Parse {

    List<Post> list(String url);
    Post detail(String link);
}
