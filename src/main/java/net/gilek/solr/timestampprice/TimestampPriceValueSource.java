package net.gilek.solr.timestampprice;

import java.io.IOException;
import java.util.Map;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.docvalues.DoubleDocValues;
import org.apache.solr.common.SolrException;

public class TimestampPriceValueSource extends ValueSource {
    private final long timestamp;
    private final ValueSource price;

    TimestampPriceValueSource(final ValueSource price, final long timestamp) {
        if (price == null){
            throw new SolrException(
                SolrException.ErrorCode.BAD_REQUEST,
                "timestamp_price: invalid value source"
            );
        }

        this.price = price;
        this.timestamp = timestamp;
    }

    @Override
    public FunctionValues getValues(Map context, LeafReaderContext readerContext) throws IOException {
        final FunctionValues priceValues = this.price.getValues(context, readerContext);

        return new
            DoubleDocValues(this) {
                @Override
                public double doubleVal(int doc) {
                    final String priceDefinition = priceValues.strVal(doc);
                    TimestampPriceParser priceParser = new TimestampPriceParser();

                    Double price;
                    try {
                        price = priceParser.parse(priceDefinition, timestamp);
                    } catch (TimestampPriceParserException exception) {
                        return 0.0d;
                    }

                    return price;
                }
            };
    }

    @Override
    public boolean equals(Object o) {
        if (this.getClass() != o.getClass()) {
            return false;
        }

        TimestampPriceValueSource other = (TimestampPriceValueSource) o;

        return this.price.equals(other.price) && this.timestamp == other.timestamp;
    }

    @Override
    public int hashCode() {
        long combinedHashes = (this.price.hashCode() + Long.hashCode(this.timestamp));

        return (int) (combinedHashes ^ (combinedHashes >>> 32));
    }

    @Override
    public String description() {
        return "Return price based on given timestamp";
    }
}
