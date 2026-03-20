package example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger2 配置类，
 * 访问路径：swagger-ui.html
 * 自动注册：
 *     位置：resources/META-INF/spring.factories
 *     内容：
 *        org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
 *          com.czxy.changgou4.config.Swagger2Configuration
 * Created by liangtong.
 */
@Configuration
@EnableSwagger2
public class Swagger2ConfigurationV3 {

    @Bean
    public Docket createRestApi() {
        // 1 确定文档Swagger版本
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        // 2 设置 api基本信息
        docket.apiInfo(apiInfo());
        // 3 设置自定义加载路径
        docket = docket.select()
                .apis(RequestHandlerSelectors.basePackage("com.javazhou"))
                .paths(PathSelectors.any())
                .build();
        //4 设置权限
        docket.securitySchemes(securitySchemes());
        docket.securityContexts(securityContexts());

        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API")
                .description("基于swagger接口文档")
                .contact(new Contact("梁桐","http://www.javaliang.com","liangtong@itcast.cn"))
                .version("1.0")
                .build();
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> list = new ArrayList<>();
        // name 为参数名  keyname是页面传值显示的 keyname， name在swagger鉴权中使用
        list.add(new ApiKey("Authorization", "Authorization", "header"));
        return list;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> list = new ArrayList<>();
        list.add(SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("^(?!auth).*$"))
                .build());
        return list;
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> list = new ArrayList();
        list.add(new SecurityReference("Authorization", authorizationScopes));
        return list;
    }

}
