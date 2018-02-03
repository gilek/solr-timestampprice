package net.gilek.solr.timestampprice;

import org.apache.lucene.queries.function.ValueSource;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.SyntaxError;
import org.apache.solr.search.ValueSourceParser;
import java.sql.Timestamp;
import org.apache.solr.common.SolrException;

public class TimestampPriceValueSourceParser extends ValueSourceParser {

    public ValueSource parse(FunctionQParser fqp) throws SyntaxError {
        ValueSource price = fqp.parseValueSource();
        String timestamp = fqp.parseArg();

        return new TimestampPriceValueSource(price, parseTimestamp(timestamp));
    }

    private long parseTimestamp(String timestamp) {
        if (timestamp != null) {
            try {
                return Long.parseLong(timestamp);
            } catch (Exception e) {
                throw new SolrException(
                    SolrException.ErrorCode.BAD_REQUEST,
                    "timestamp_price: could not parse given timestamp"
                );
            }
        }

        return getCurrentTimestamp();
    }

    private long getCurrentTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        return timestamp.getTime() / 1000;
    }
}
