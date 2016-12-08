package com.turel.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.TopicMetadataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;


/**
 * Created by chaimturkel on 12/6/16.
 *
* The service will send metadataRequest to defined topics in Kafka and logout the response
*/
public class MonitorService {

    private static final int BUFFER_SIZE = 64 * 1024;
    private static final int SO_TIMEOUT = 5000; // 5 seconds
    static final private Logger logger = LoggerFactory.getLogger(MonitorService.class);


    private final TopicMetadataRequest metadataRequest;
    private final List<String> zookeeperHosts;

    public MonitorService(List<String> zookeeperHosts, List<String> topics) {

        metadataRequest = new TopicMetadataRequest(topics);
        this.zookeeperHosts = zookeeperHosts;
    }

    private static ObjectNode generateAdditionalInfo(TopicMetadataResponse topicMetadataResponse, String brokerHost) {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode node = objectMapper.readTree(topicMetadataResponse.toString());
            return null;
//            return Builder.objectNode().set("metadata", node).set("brokerHost", brokerHost).toObjectNode();
        } catch (IOException e) {
            return null;
        }
    }

//    public void sendRequest() {
//
//        List<String> kafkaBrokersList = new KafkaBrokersList(zookeeperHosts).getHosts();
//
//        if(kafkaBrokersList.isEmpty()){
////            logger.error(Builder.objectNode().set("error", "empty brokers list").toObjectNode());
//            return;
//        }
//
//        kafkaBrokersList.forEach(brokerHost -> {
//
//                    String[] host = brokerHost.split(":");
//                    String address = host[0];
//                    int port = Integer.parseInt(host[1]);
//
//                    SimpleConsumer consumer = new SimpleConsumer(
//                            address, port, SO_TIMEOUT, BUFFER_SIZE, "monitoringService"
//                    );
//
//                    try {
//
//                        TopicMetadataResponse topicMetadataResponse = consumer.send(metadataRequest);
////                        logger.info(generateAdditionalInfo(topicMetadataResponse, brokerHost));
//
//                    } catch (Throwable t) {
////                        logger.error(Builder.objectNode().set("brokerHost", brokerHost).toObjectNode(), t);
//                    }
//                }
//        );
//    }
}

