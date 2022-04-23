//package org.elasticsearch;
//
//import org.elasticsearch.action.get.GetResponse;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.action.update.UpdateRequest;
//import org.elasticsearch.action.update.UpdateResponse;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.TransportAddress;
//import org.elasticsearch.common.xcontent.XContentFactory;
//import org.elasticsearch.common.xcontent.XContentType;
//import org.elasticsearch.transport.client.PreBuiltTransportClient;
//
//
//import java.io.IOException;
//import java.net.InetAddress;
//import java.util.Date;
//import java.util.concurrent.ExecutionException;
//
//public class EsClient {
//    private static final String host_id = "127.0.0.1";
//    private static TransportClient client;
//
//    /**
//     * 初始化es client
//     */
//    static {
//        Settings setting = Settings.builder()
//                .put("cluster.name", "elasticsearch")
//                .put("client.transport.sniff", true)
//                .build();
//        try {
//            InetAddress ia1=InetAddress.getByName(host_id);
//            client = new PreBuiltTransportClient(setting)
//                    .addTransportAddress(new TransportAddress(ia1, 9300));
//            //.addTransportAddress(new TransportAddress(InetAddress.getByName("host2"), 9300));
//        } catch (java.net.UnknownHostException e) {
//            System.out.println(e);
//        }
//
//    }
//
//    public static void main(String args[]) {
//
//        String json = "{\"first_name\":\"finerliu\",\"last_name\":\"f\",\"age\":28,\"about\":\"I like sport.\",\"interests\":[\"sport\",\"music\"]}";
//        //System.out.println(json);
//
//
//        //put(json);
//        post();
//        //updateOrAdd();
//        get();
//        close();
//    }
//
//    /**
//     * 通过手动json 添加数据
//     *
//     * @param json
//     */
//    public static void put(String json) {
//        IndexResponse response = client.prepareIndex("youindex", "employee","1")
//                .setSource(json, XContentType.JSON).get();
//        System.out.println(response);
//    }
//
//    /**
//     * 查询数据
//     */
//    public static void get() {
//        GetResponse getResponse = client.prepareGet("youindex", "employee", "1").get();
//        System.out.println(getResponse);
//    }
//
//    /**
//     * 通过XContentFactory 方式添加
//     */
//    public static void post() {
//        try {
//            IndexResponse response = client.prepareIndex("youindex", "employee","1")
//                    .setSource(XContentFactory.jsonBuilder()
//                            .startObject()
//                            .field("first_name", "pikaqiu")
//                            .field("last_name", "pika")
//                            .field("age", 33)
//                            .field("about", "i like u")
//                            .field("interests", "sport")
//                            .field("date", new Date())
//                            .endObject()).get();
//            System.out.println(response);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 更新或者添加
//     * 如果存在则更新，不存在添加
//     */
//    public static void updateOrAdd() {
//        try {
//            IndexRequest indexRequest = new IndexRequest("you index", "employee", "15")
//                    .source(XContentFactory.jsonBuilder()
//                            .startObject()
//                            .field("imac", "10.14.5")
//                            .field("name", "小苹果.")
//                            .endObject());
//
//            UpdateRequest updateRequest = new UpdateRequest("you index", "employee", "15")
//                    .doc(XContentFactory.jsonBuilder()
//                            .startObject()
//                            .field("imac", "10.14")
//                            .endObject())
//                    .upsert(indexRequest);
//            UpdateResponse updateResponse = client.update(updateRequest).get();
//            System.out.println(updateResponse);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 关闭连接
//     */
//    public static void close() {
//        client.close();
//    }
//}
