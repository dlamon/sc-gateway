package cn.net.liaowei.sc.gateway.filter.factory;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author liaowei
 */
@Component
public class ProductGatewayFilterFactory extends AbstractGatewayFilterFactory<ProductGatewayFilterFactory.Config> {
    public static final String AUTHORIZE_KEY = "enabled";
    private static final String PRODUCT_ID = "productId";

    public ProductGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(AUTHORIZE_KEY);
    }

    @Override
    public GatewayFilter apply(ProductGatewayFilterFactory.Config config) {
        return (exchange, chain) -> {
            if(!config.isEnabled()) {
                return chain.filter(exchange);
            }
            ServerHttpRequest serverHttpRequest = exchange.getRequest();
            HttpHeaders httpHeaders = serverHttpRequest.getHeaders();
            String token = httpHeaders.getFirst(PRODUCT_ID);
            ServerHttpResponse response = exchange.getResponse();
            if(StringUtils.isEmpty(token)) {
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                return response.setComplete();
            }
            return chain.filter(exchange);
        };
    }

    @Getter
    @Setter
    public static class Config {
        private boolean enabled;
    }
}
