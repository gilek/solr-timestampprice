package net.gilek.solr.timestampprice;

import org.apache.solr.common.SolrException;

public class TimestampPriceParserException extends SolrException {
    TimestampPriceParserException(String message) {
        super(SolrException.ErrorCode.BAD_REQUEST, message);
    }
}
