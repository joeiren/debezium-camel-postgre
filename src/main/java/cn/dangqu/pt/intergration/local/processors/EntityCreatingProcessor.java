package cn.dangqu.pt.intergration.local.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.debezium.DebeziumConstants;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("creatingProcessor")
public class EntityCreatingProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        final Map value = exchange.getMessage().getHeader(DebeziumConstants.HEADER_BEFORE, Map.class);
    }
}
