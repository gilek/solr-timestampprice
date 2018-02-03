package net.gilek.solr.timestampprice;

public class TimestampPriceParser {

    TimestampPriceParser() {
    }

    Double parse(final String value, final long timestamp) throws TimestampPriceParserException {
        Double price = null;

        if (value == null || value.trim().length() == 0) {
            throw new TimestampPriceParserException("Incorrect definition parameter.");
        }

        if (timestamp < 0) {
            throw new TimestampPriceParserException("Incorrect timestamp.");
        }

        String[] series = value.split("\\s+");
        if (series.length == 0) {
            throw new TimestampPriceParserException("Lack of definition");
        }

        long lastTimestamp = 0;
        for (String rawPriceInterval : series) {
            String[] priceInterval = rawPriceInterval.split("\\|");
            if (priceInterval.length != 2) {
                throw new TimestampPriceParserException(
                    String.format("Incorrect definition '%s' format.", rawPriceInterval)
                );
            }

            long localTimestamp;
            try {
                localTimestamp = Long.parseLong(priceInterval[0].trim());
            } catch (Exception e) {
                throw new TimestampPriceParserException("Incorrect timestamp.");
            }

            if (localTimestamp <= timestamp && localTimestamp > lastTimestamp) {
                try {
                    price = Double.parseDouble(priceInterval[1].trim());
                } catch (Exception e) {
                    throw new TimestampPriceParserException("Incorrect price.");
                }

                lastTimestamp = localTimestamp;
            }
        }

        return price;
    }
}
