package cn.dangqu.pt.intergration.local;

import cn.dangqu.pt.intergration.local.processors.EntityCreatingProcessor;
import cn.dangqu.pt.intergration.local.processors.EntityRemovingProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.debezium.DebeziumConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PgRouter extends RouteBuilder {
    @Autowired
    EntityCreatingProcessor creatingProcessor;
    @Autowired
    EntityRemovingProcessor removingProcessor;

    @Override
    public void configure() throws Exception {
        from("debezium-postgres:pgSQL13-connector?"
                + "databaseHostname=localhost"
                + "&databasePort=5432"
                + "&databaseUser=postgres"
                + "&databasePassword=password"
                + "&databaseServerName=pgSQL13"
                + "&databaseHistoryFileFilename={{debezium.postgreSql.dbHistoryFile}}"
                + "&databaseDbname=cn_lumi"
                + "&tableIncludeList=public.products"
                + "&offsetStorageFileName={{debezium.postgreSql.dbOffsetFile}}"
                + "&pluginName=pgoutput"
                )
                .log("Event received from Debezium : ${body}")
                .log("    with this identifier ${headers.CamelDebeziumIdentifier}")
                .log("    with these source metadata ${headers.CamelDebeziumSourceMetadata}")
                .log("    the event occured upon this operation '${headers.CamelDebeziumSourceOperation}'")
                .log("    on this database '${headers.CamelDebeziumSourceMetadata[db]}' and this table '${headers.CamelDebeziumSourceMetadata[table]}'")
                .log("    with the key ${headers.CamelDebeziumKey}")
                .log("    the previous value is ${headers.CamelDebeziumBefore}")
                .choice()
                    .when(header(DebeziumConstants.HEADER_OPERATION).isEqualTo("c"))
                        .process(creatingProcessor)
                        //.to("rest-swagger:http://localhost:8082/v2/api-docs#addOrderUsingPOST")
                        .to("stream:out")
                    .when(header(DebeziumConstants.HEADER_OPERATION).isEqualTo("d"))
                        .process(removingProcessor)
                        //.to("rest-swagger:http://localhost:8082/v2/api-docs#deleteOrderUsingDELETE")
                        .to("stream:out")
                        .log("Response : ${body}");
    }
}
