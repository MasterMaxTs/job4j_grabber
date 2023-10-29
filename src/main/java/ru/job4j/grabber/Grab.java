package ru.job4j.grabber;

import org.quartz.SchedulerException;

/**
 * Загрузка распарсенных постов в хранилище по расписанию
 */
public interface Grab {

    /**
     * Инициализирует процесс загрузки постов c сайта
     */
    void init() throws SchedulerException;
}
