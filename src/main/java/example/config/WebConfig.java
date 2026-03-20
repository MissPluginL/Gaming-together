package example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/api/**") // 拦截所有接口
                .excludePathPatterns(
                        "/api/user/login",    // 放行登录
                        "/api/user/register", // 放行注册
                        "/api/post/list",     // 放行帖子列表
                        "/api/player/list",   // 放行陪玩列表
                        "/api/evaluate/list/**"
                );
    }
}