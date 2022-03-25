package ru.job4j;

import java.util.List;

public interface Parse {

    List<Post> list(String url);
    Post detail(String link);
}
