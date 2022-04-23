package com.ys.mail;

import com.ys.mail.mapper.UmsUserMapper;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author 24
 * @date 2021/12/31 10:06
 * @description ES创建索引库
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MailPortalApplication.class})
public class ElasticSerachTest {


    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private UmsUserMapper umsUserMapper;


    /**
     * 执行这段代码创建ES索引库,先执行1,再执行2,再放开每月@PostConstruct
     *
     * @throws IOException
     */
    @Test
    public void createMapping() throws IOException {
        IndicesClient indices = client.indices();
        CreateIndexRequest request = new CreateIndexRequest("game_record");
        String source = "{\n" +
                "    \"properties\": {\n" +
                "      \"gameDate\":{\n" +
                "        \"type\": \"integer\",\n" +
                "        \"index\": true\n" +
                "      },\n" +
                "      \"earnings\":{\n" +
                "        \"properties\": {\n" +
                "          \"date\":{\n" +
                "            \"type\":\"integer\"\n" +
                "          },\n" +
                "          \"earning\":{\n" +
                "            \"type\":\"double\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "}\n";
        request.mapping(source, XContentType.JSON);
        CreateIndexResponse response = indices.create(request, RequestOptions.DEFAULT);
        System.out.println(response.isAcknowledged());
    }

    /**
     * 执行这段代码创建ES索引库
     *
     * @throws IOException
     */
    @Test
    public void createMapping2() throws IOException {
        IndicesClient indices = client.indices();
        CreateIndexRequest request = new CreateIndexRequest("group_master");
        String source = "{\n" +
                " \"properties\": {\n" +
                "  \"gameDate\": {\n" +
                "   \"type\": \"integer\"\n" +
                "  },\n" +
                "  \"earnings\": {\n" +
                "   \"properties\": {\n" +
                "    \"date\": {\n" +
                "     \"type\": \"integer\"\n" +
                "    },\n" +
                "    \"ratio\": {\n" +
                "     \"type\": \"double\"\n" +
                "    },\n" +
                "    \"level\": {\n" +
                "     \"type\": \"integer\"\n" +
                "    }\n" +
                "   }\n" +
                "  }\n" +
                " }\n" +
                "}";
        request.mapping(source, XContentType.JSON);
        CreateIndexResponse response = indices.create(request, RequestOptions.DEFAULT);
        System.out.println(response.isAcknowledged());
    }
}
