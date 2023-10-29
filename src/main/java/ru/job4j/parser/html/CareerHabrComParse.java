package ru.job4j.parser.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.model.Post;
import ru.job4j.parser.DateTimeParser;
import ru.job4j.parser.Parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация парсинга сайта: career.habr.com
 */
public class CareerHabrComParse implements Parse {

    /**
     * Инициализация строкового значения URL-адреса сайта
     */
    private static final String SOURCE_LINK = "https://career.habr.com/";

    /**
     * Инициализация строкового значения пути к странице сайта
     */
    private static final String PAGE_LINK = "vacancies/java_developer";

    /**
     *  Инициализация количества первых страниц сайта для парсинга
     */
    private static final int PAGE_COUNT = 6;

    /**
     * Строковое значение URL-адреса страницы сайта
     */
    private final String url;

    /**
     * Зависимость от парсера даты
     */
    private final DateTimeParser dateTimeParser;

    /**
     * Конструктор
     * @param dateTimeParser парсер даты
     */
    public CareerHabrComParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
        url = String.format("%s%s", SOURCE_LINK, PAGE_LINK);
    }

    @Override
    public List<Post> list() {
        List<Post> vacancies = new ArrayList<>();
        for (int i = 1; i < PAGE_COUNT; i++) {
            try {
                Document doc = Jsoup.connect(String.format(url + "?page=%s", i)).get();
                Elements rows = doc.select(".vacancy-card__inner");
                rows.forEach(row -> {
                    Element linkElement = row.select(".vacancy-card__title")
                                            .first()
                                            .child(0);
                    String link = String.format("%s%s",
                            SOURCE_LINK, linkElement.attr("href"));
                    vacancies.add(
                            this.detail(link)
                    );
                });

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return vacancies;
    }

    @Override
    public Post detail(String link) {
        Post post = new Post();
        Elements rows;
        try {
            Document doc = Jsoup.connect(link).get();
            rows = doc.select(".page-title__title");
            post.setTitle(rows.first().text());
            post.setLink(link);
            rows = doc.select(".style-ugc");
            post.setDescription(rows.first().children().get(0).text());
            rows = doc.select(".basic-date");
            post.setCreated(
                    dateTimeParser.parse(rows.first().attr("datetime")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return post;
    }
}
