package net.gilek.solr.timestampprice;

import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Map;
import java.util.LinkedHashMap;

public class TimestampPriceParserTest {
    @Test
    public void testOneDefinition() {
        TimestampPriceParser parser = new TimestampPriceParser();
        long actualTimestamp = getCurrentTimestamp();
        String definition = String.valueOf(actualTimestamp - 1) + '|' + 100.0;
        double result = parser.parse(definition, actualTimestamp);

        assertEquals(result, 100.0, 0);
    }

    private long getCurrentTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return timestamp.getTime() / 1000;
    }

    @Test
    public void testMultipleDefinitions() {
        long actualTimestamp = getCurrentTimestamp();

        Map<Long, Double> parts = new LinkedHashMap<>();
        parts.put(actualTimestamp - 1000, 10.0);
        parts.put(actualTimestamp, 20.0);
        parts.put(actualTimestamp + 1000, 30.0);

        StringBuilder definition = new StringBuilder();
        for (Map.Entry<Long, Double> entry : parts.entrySet()) {
            definition.append(entry.getKey())
                    .append("|")
                    .append(entry.getValue())
                    .append (" ");
        }

        TimestampPriceParser parser = new TimestampPriceParser();
        double result = parser.parse(definition.toString().trim(), actualTimestamp);

        assertEquals(result,20.0, 0);
    }

    @Test(expected = TimestampPriceParserException.class)
    public void testLackDefinition() {
        TimestampPriceParser parser = new TimestampPriceParser();
        parser.parse("", getCurrentTimestamp());
    }

    @Test(expected = TimestampPriceParserException.class)
    public void testLackOfTimestamp() {
        TimestampPriceParser parser = new TimestampPriceParser();
        parser.parse("1:1.0", -1);
    }
}
