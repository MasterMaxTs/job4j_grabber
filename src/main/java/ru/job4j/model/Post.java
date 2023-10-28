package ru.job4j.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Модель данных Пост
 */
public class Post {

    /**
     * Идентификатор вакансии
     */
    private int id;

    /**
     * Название вакансии
     */
    private String title;

    /**
     * Ссылка на описание вакансии
     */
    private String link;

    /**
     * Описание вакансии
     */
    private String description;

    /**
     * Дата создания вакансии
     */
    private LocalDateTime created;

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format(
                            "id = %d;"
                            + "\ntitle = %s;"
                            + "\nlink = %s;"
                            + "\ndescription = %s;"
                            + "\ncreated = %s.\n",
                            id, title, link, description, created
        );
    }
}