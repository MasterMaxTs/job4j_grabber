package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.Parse;
import ru.job4j.Post;
import ru.job4j.utils.DateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlRuParse implements Parse {

    private final DateTimeParser dateTimeParser;
    private static final int COUNT_OF_PAGES = 5;
    private static final String SEARCH = "java";

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

    private boolean match(String link) {
        Pattern pattern = Pattern.compile(
                                         String.format(".*\\b%s\\b.*", SEARCH));
        Matcher matcher = pattern.matcher(link);
        return matcher.matches();
    }
}
