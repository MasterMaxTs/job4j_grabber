package ru.job4j.parser;

import java.time.LocalDateTime;

/**
 * Парсинг даты на сайте
 */
public interface DateTimeParser {
    LocalDateTime parse(String parse);
}
