package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.model.Post;
import ru.job4j.parser.Parse;
import ru.job4j.store.Store;

import java.util.List;
import java.util.Properties;

public class Grabber implements Grab {

    private static final String PARSE_KEY_DATA_MAP = "parse";
    private static final String STORE_KEY_DATA_MAP = "store";
    private static final String INTERVAL_IN_HOURS = "time";
    private static final String PARSING_SITE_URL = "site.url";
    private static String url = "";
    private final Properties cfg;

    public Grabber(Properties cfg) {
        this.cfg = cfg;
        url = cfg.getProperty(PARSING_SITE_URL);
    }

    public static void showResult(Store store) {
        store.getAll().forEach(System.out::println);
    }

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
