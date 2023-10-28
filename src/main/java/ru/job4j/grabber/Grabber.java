package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.model.Post;
import ru.job4j.parser.Parse;
import ru.job4j.store.Store;

import java.util.List;
import java.util.Properties;

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
     * Значение интервала в часах между запусками выполнения задания
     */
    private static final String INTERVAL_IN_HOURS = "time";

    /**
     * Значение ключа для объекта Properties
     */
    private static final String PARSING_SITE_URL = "site.url";

    /**
     * Значение ссылки url
     */
    private static String url = "";

    /**
     * Зависимость от объекта свойства приложения
     */
    private final Properties cfg;

    /**
     * Конструктор
     * @param cfg объект свойства приложения
     */
    public Grabber(Properties cfg) {
        this.cfg = cfg;
        url = cfg.getProperty(PARSING_SITE_URL);
    }

    /**
     * Выводит все посты из хранилища в консоль
     * @param store объект хранилище постов
     */
    public static void showResult(Store store) {
        store.getAll().forEach(System.out::println);
    }

    /**
     * Создаёт и запускает планировщик
     * @return объект планировщик
     */
    public Scheduler scheduler() {
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
     * @param parse объект парсинг сайта
     * @param store объект хранилище постов
     * @param scheduler объект планировщик
     */
    @Override
    public void init(Parse parse, Store store, Scheduler scheduler)
                                                    throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put(PARSE_KEY_DATA_MAP, parse);
        data.put(STORE_KEY_DATA_MAP, store);
        JobDetail job = JobBuilder
                                 .newJob(GrabJob.class)
                                 .usingJobData(data)
                                 .build();
        SimpleScheduleBuilder times = SimpleScheduleBuilder.repeatHourlyForever(
                Integer.parseInt(cfg.getProperty(INTERVAL_IN_HOURS))
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
            List<Post> posts = parse.list(url);
            posts.forEach(store::save);
            showResult(store);
        }
    }
}
