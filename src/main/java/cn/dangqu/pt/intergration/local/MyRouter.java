package cn.dangqu.pt.intergration.local;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class MyRouter extends RouteBuilder {

    @Autowired
    MyBean myBean;

    @Override
    public void configure() throws Exception {
        from("timer:hello?period={{myPeriod}}").routeId("hello")
                // and call the bean
                .bean(myBean, "saySomething")
                // and print it to system out via stream component
                .to("stream:out");
    }
}
