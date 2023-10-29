package ru.job4j.parser.html;

import ru.job4j.parser.DateTimeParser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

/**
 * Реализация парсера даты на сайте: sql.ru
 */
public class SqlRuDateTimeParser implements DateTimeParser {

    /**
     * Инициализация текущей даты
     */
    private static final LocalDate CURRENT_DATE = LocalDate.now();

    /**
     * Инициализация строкового представления сегодняшней даты
     */
    private static final String TODAY = "сегодня";

    /**
     * Инициализация строкового представления вчерашней даты
     */
    private static final String YESTERDAY = "вчера";

    /**
     *  Инцициализация префикса для отображения года в дате
     */
    private static final String PREFIX = "20";

    /**
     * Инициализация значения текущей позиции индекса массива
     */
    private static final int POS = 0;

    /**
     * Инициализация карты соответствия названия даты
     * и порядкового номера месяца
     */
    private static final Map<String, String> MONTHS =
            Map.ofEntries(
                    Map.entry(TODAY,
                            String.valueOf(CURRENT_DATE.getMonth().getValue())
                    ),
                    Map.entry(YESTERDAY,
                            String.valueOf(CURRENT_DATE.minusDays(1).getMonth().getValue())
                    ),
                    Map.entry("янв", "01"),
                    Map.entry("фев", "02"),
                    Map.entry("мар", "03"),
                    Map.entry("апр", "04"),
                    Map.entry("май", "05"),
                    Map.entry("июн", "06"),
                    Map.entry("июл", "07"),
                    Map.entry("авг", "08"),
                    Map.entry("сен", "09"),
                    Map.entry("окт", "10"),
                    Map.entry("ноя", "11"),
                    Map.entry("дек", "12")
            );

    @Override
    public LocalDateTime parse(String parse) {
        int year;
        int month;
        int dayOfMonth;
        String[] elements = parse.split(",");
        LocalTime time =
                LocalTime.parse(elements[POS + 1].replaceAll("[\\[\\]|\\s]",
                        ""));
        String[] dateComponents = elements[POS].split("\\s");
        if (dateComponents.length == 1) {
            year = CURRENT_DATE.getYear();
            month = Integer.parseInt(MONTHS.get(dateComponents[POS]));
            dayOfMonth =
                    dateComponents[POS].equals(YESTERDAY)
                            ? CURRENT_DATE.minusDays(1).getDayOfMonth()
                            : CURRENT_DATE.getDayOfMonth();

        } else {
            year = Integer.parseInt(PREFIX.concat(dateComponents[POS + 2]));
            month = Integer.parseInt(MONTHS.get(dateComponents[POS + 1]));
            dayOfMonth = Integer.parseInt(dateComponents[POS]);
        }
        return LocalDateTime.of(
                LocalDate.of(year, month, dayOfMonth),
                time
        );
    }
}
