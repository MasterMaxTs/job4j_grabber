package ru.job4j.util;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.parser.html.SqlRuDateTimeParser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SqlRuDateTimeParserTest {

    private static LocalDate dateNow;
    private static LocalDate dateYesterday;

    @Before
    public void setDate() {
        dateNow = LocalDate.now();
        dateYesterday = LocalDate.now().minusDays(1);
    }

    @Test
    public void parse() {
        List<String> input = List.of(
                                    "17 май 20, 12:30",
                                    "сегодня,      20:14",
                                    "вчера,09:00"
        );
        SqlRuDateTimeParser sqlRuDtp = new SqlRuDateTimeParser();
        List<LocalDateTime> rsl = List.of(
                sqlRuDtp.parse(input.get(0)),
                sqlRuDtp.parse(input.get(1)),
                sqlRuDtp.parse(input.get(2))
        );
        List<LocalDateTime> expected = List.of(
                LocalDateTime.of(2020, 5, 17, 12, 30),
                LocalDateTime.of(dateNow, LocalTime.of(20, 14)),
                LocalDateTime.of(dateYesterday, LocalTime.of(9, 0))
        );
        assertEquals(rsl.get(0), expected.get(0));
        assertEquals(rsl.get(1), expected.get(1));
        assertEquals(rsl.get(2), expected.get(2));
    }
}