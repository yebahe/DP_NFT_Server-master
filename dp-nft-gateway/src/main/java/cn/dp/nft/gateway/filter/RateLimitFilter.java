package cn.dp.nft.gateway.filter;


import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class RateLimitFilter extends Filter {
    @Override
    public FilterReply decide(Object o) {
        return null;
    }
}
