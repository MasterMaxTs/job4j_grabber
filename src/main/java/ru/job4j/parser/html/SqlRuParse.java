package ru.job4j.parser.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.model.Post;
import ru.job4j.parser.DateTimeParser;
import ru.job4j.parser.Parse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Реализация парсинга сайта: sql.ru
 */
public class SqlRuParse implements Parse {

    /**
     * Количество первых страниц сайта для парсинга
     */
    private static final int COUNT_OF_PAGES = 5;

    /**
     * Критерий поиска вакансии
     */
    private static final String SEARCH = "java";

    /**
     * Зависимость от парсинга даты
     */
    private final DateTimeParser dateTimeParser;

    /**
     * Конструктор
     * @param dateTimeParser реализация парсинга даты на сайте
     */
    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String url) {
        List<Post> vacancies = new ArrayList<>();
        for (int i = 1; i <= COUNT_OF_PAGES; i++) {
            try {
                Document doc = Jsoup.connect(
                        String.format(url + "%s", i)).get();
                Elements row = doc.select(".postslisttopic");
                for (Element td
                        : row) {
                    String link = td.child(0).attr("href").toLowerCase();
                    if (this.match(link)) {
                        vacancies.add(
                                detail(link)
                        );
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return vacancies;
    }

    @Override
    public Post detail(String link) {
        Post post = new Post();
        Elements row;
        try {
            Document doc = Jsoup.connect(link).get();
            row = doc.select(".messageHeader");
            post.setTitle(row.get(0).ownText());
            post.setLink(link);
            row = doc.select(".msgBody");
            post.setDescription(row.get(1).text());
            row = doc.select(".msgFooter");
            LocalDateTime created =
                    this.dateTimeParser.parse(row.get(0).ownText());
            post.setCreated(created);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return post;
    }

    /**
     * Проверяет, содержит ли ссылка на пост искомый критерий поиска вакансии
     * @param link ссылка на ресурс сайта с постом на входе
     * @return результат проверки в виде boolean
     */
    private boolean match(String link) {
        Pattern pattern = Pattern.compile(
                                         String.format(".*\\b%s\\b.*", SEARCH));
        Matcher matcher = pattern.matcher(link);
        return matcher.matches();
    }
}
