package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.model.Post;
import ru.job4j.parser.Parse;
import ru.job4j.store.Store;

import java.util.List;

/**
 * Реализация загрузки распарсенных постов в хранилище по расписанию
 */
public class Grabber implements Grab {

    /**
     * Значение ключа для объекта JobDataMap
     */
    private static final String PARSE_KEY_DATA_MAP = "parse";

    /**
     * Значение ключа для объекта JobDataMap
     */
    private static final String STORE_KEY_DATA_MAP = "store";

    /**
     * Зависимость от парсера сайта
     */
    private final Parse parser;

    /**
     * Зависимость от хранилища постов
     */
    private final Store store;

    /**
     * Объект планировщик
     */
    private final Scheduler scheduler;

    /**
     * Значение интервала в часах между запусками выполнения задания
     */
    private final String time;

    /**
     * Конструктор
     * @param parser парсер сайта
     * @param store хранилище постов
     * @param time часовой интервал между запусками
     */
    public Grabber(Parse parser, Store store, String time) {
        this.parser = parser;
        this.store = store;
        this.time = time;
        scheduler = initScheduler();
    }

    /**
     * Выводит все посты из хранилища в консоль
     * @param store объект хранилище постов
     */
    public static void showResult(Store store) {
        store.getAll().forEach(System.out::println);
    }

    /**
     * Инициализация и запуск планировщика
     * @return объект планировщик
     */
    private Scheduler initScheduler() {
        Scheduler scheduler = null;
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return scheduler;
    }

    /**
     * Инициализирует загрузку распарсенных постов в хранинилище по расписанию
     */
    @Override
    public void init() throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put(PARSE_KEY_DATA_MAP, parser);
        data.put(STORE_KEY_DATA_MAP, store);
        JobDetail job = JobBuilder
                                 .newJob(GrabJob.class)
                                 .usingJobData(data)
                                 .build();
        SimpleScheduleBuilder times = SimpleScheduleBuilder.repeatHourlyForever(
                Integer.parseInt(time)
        );
        Trigger trigger = TriggerBuilder
                                        .newTrigger()
                                        .startNow()
                                        .withSchedule(times)
                                        .build();
        scheduler.scheduleJob(job, trigger);
    }

    /**
     * Вспомогательный класс, задающий работу планировщику
     */
    public static class GrabJob implements Job {

        @Override
        public void execute(JobExecutionContext context) {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Parse parse = (Parse) map.get(PARSE_KEY_DATA_MAP);
            Store store = (Store) map.get(STORE_KEY_DATA_MAP);
            List<Post> posts = parse.list();
            posts.forEach(store::save);
            showResult(store);
        }
    }
}
