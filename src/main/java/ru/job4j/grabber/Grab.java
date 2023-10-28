package ru.job4j.grabber;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import ru.job4j.parser.Parse;
import ru.job4j.store.Store;

/**
 * Загрузка распарсенных постов в хранилище по расписанию
 */
public interface Grab {

    /**
     * Инициализирует процесс загрузки постов
     * @param parse объект парсинг сайта
     * @param store объект хранилище постов
     * @param scheduler объект планировщик
     */
    void init(Parse parse, Store store, Scheduler scheduler)
                                                throws SchedulerException;
}
