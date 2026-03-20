package example.config;


import example.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头获取 token
        String token = request.getHeader("token");
        if (token == null || token.trim().isEmpty()) {
            response.setStatus(401);
            return false;
        }

        // 解析
        Claims claims = JwtUtil.parseToken(token);
        if (claims == null) {
            response.setStatus(401);
            return false;
        }

        // 把 userId 存入 request，Controller 直接取用
        Long userId = Long.valueOf(claims.get("userId").toString());
        request.setAttribute("userId", userId);
        return true;
    }
}