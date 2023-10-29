package ru.job4j.parser;

import java.time.LocalDateTime;

/**
 * Парсинг даты на сайте
 */
public interface DateTimeParser {

    /**
     * Парсит строковое представление даты и преобразует в нужный тип
     * @param parse строковое значение даты
     * @return преобразованную дату
     */
    LocalDateTime parse(String parse);
}
