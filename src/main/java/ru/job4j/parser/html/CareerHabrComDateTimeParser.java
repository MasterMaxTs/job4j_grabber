package ru.job4j.parser.html;

import ru.job4j.parser.DateTimeParser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Реализация парсера даты на сайте: career.habr.com
 */
public class CareerHabrComDateTimeParser implements DateTimeParser {

    /**
     * Инициализация объекта DateTimeFormatter
     */
    private static final DateTimeFormatter FORMATTER =
                                    DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Инициализация значения текущей позиции индекса массива
     */
    private static final int POS = 0;

    @Override
    public LocalDateTime parse(String parse) {
        String[] strings = parse.split("\\+");
        return LocalDateTime.from(FORMATTER.parse(strings[POS]));
    }
}
