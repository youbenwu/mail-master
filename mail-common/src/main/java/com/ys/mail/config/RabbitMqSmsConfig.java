package com.ys.mail.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMq ms 配置
 */
@Configuration
public class RabbitMqSmsConfig {

    public static final String MS_EXCHANGE = "ms.exchange";
    public static final String MS_QUEUE = "ms.queue";
    public static final String MS_ROUTING_KEY = "ms.routing.key";

    public static final String DLX_QUEUE_MS = "dlx_queue_ms";
    public static final String DLX_EXCHANGE_MS = "dlx_exchange_ms";
    public static final String DLX_ROUTING_KEY_MS = "dlx_routing_key_ms";

    public static final String CHANGE_ACTIVE_EXCHANGE = "change.active.exchange";
    public static final String CHANGE_ACTIVE_QUEUE = "change.active.queue";
    public static final String CHANGE_ACTIVE_ROUTING_KEY = "change.active.routing.key";


    /**
     * 死信队列
     *
     * @return
     */
    @Bean
    public Queue dlxQueue() {
        return new Queue(DLX_QUEUE_MS, true, false, false);
    }

    /**
     * 死信交换机
     *
     * @return
     */
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE_MS, true, false);
    }


    /**
     * 声明交换机
     */
    @Bean
    public DirectExchange msExchange() {
        return new DirectExchange(MS_EXCHANGE, true, false);
    }


    @Bean
    public Queue changeActiveQueue() {
        return new Queue(CHANGE_ACTIVE_QUEUE, true, false, false);
    }

    @Bean
    public DirectExchange changeActiveExchange() {
        return new DirectExchange(CHANGE_ACTIVE_EXCHANGE, true, false);
    }


    /**
     * 普通消息队列
     *
     * @return
     */
    @Bean
    public Queue dlxMsQueue() {
        Map<String, Object> args = new HashMap<>();
        //设置消息过期时间
        args.put("x-message-ttl", 1000 * 60 * 5);
        //设置死信交换机
        args.put("x-dead-letter-exchange", DLX_EXCHANGE_MS);
        //设置死信 routing_key
        args.put("x-dead-letter-routing-key", DLX_ROUTING_KEY_MS);
        return new Queue(MS_QUEUE, true, false, false, args);
    }

    /**
     * 绑定普通队列和与之对应的交换机
     *
     * @return
     */
    @Bean
    public Binding msBinding() {
        return BindingBuilder.bind(dlxMsQueue())
                .to(msExchange())
                .with(MS_ROUTING_KEY);
    }

    /**
     * 绑定死信队列和死信交换机
     *
     * @return
     */
    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue()).to(dlxExchange())
                .with(DLX_ROUTING_KEY_MS);
    }

    /**
     * 绑定普通队列和与之对应的交换机
     *
     * @return
     */
    @Bean
    public Binding changeActiveBinding() {
        return BindingBuilder.bind(changeActiveQueue())
                .to(changeActiveExchange()).with(CHANGE_ACTIVE_ROUTING_KEY);
    }
}