package ru.job4j.parser.html;

import ru.job4j.parser.DateTimeParser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {

    private static final Map<String, String> MONTHS =
            Map.ofEntries(
                    Map.entry("сегодня",
                            String.valueOf(LocalDate.now().getMonth().getValue())
                    ),
                    Map.entry("вчера",
                            String.valueOf(LocalDate.now().minusDays(1).getMonth().getValue())
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
    private static final int POS = 0;
    private static final String YESTERDAY = "вчера";
    private static final String PREFIX = "20";

    @Override
    public LocalDateTime parse(String parse) {
        int year;
        int month;
        int dayOfMonth;
        String[] elements = parse.split(",");
        LocalTime time =
                LocalTime.parse(elements[POS + 1].replaceAll("[\\[\\]|\\s]",
                        ""));
        String[] dateChars = elements[POS].split("\\s");
        if (dateChars.length == 1) {
            year = LocalDate.now().getYear();
            month = Integer.parseInt(MONTHS.get(dateChars[POS]));
            dayOfMonth =
                    dateChars[POS].equals(YESTERDAY)
                            ? LocalDate.now().minusDays(1).getDayOfMonth()
                            : LocalDate.now().getDayOfMonth();

        } else {
            year = Integer.parseInt(PREFIX.concat(dateChars[POS + 2]));
            month = Integer.parseInt(MONTHS.get(dateChars[POS + 1]));
            dayOfMonth = Integer.parseInt(dateChars[POS]);
        }
        return LocalDateTime.of(
                LocalDate.of(year, month, dayOfMonth),
                time
        );
    }
}
