package filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("LogFilter 초기화...");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;

        // 요청 시작 시간
        long startTime = System.currentTimeMillis();

        // 접근 정보 수집
        String ip      = request.getRemoteAddr();
        String url     = request.getRequestURI();
        String method  = request.getMethod();
        String time    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // 다음 필터 or 서블릿 실행
        chain.doFilter(req, res);

        // 처리 시간 계산
        long endTime     = System.currentTimeMillis();
        long processTime = endTime - startTime;

        // 콘솔에 로그 출력
        System.out.println("[LOG] " + time
                + " | IP: "      + ip
                + " | Method: "  + method
                + " | URL: "     + url
                + " | 처리시간: " + processTime + "ms");
    }

    @Override
    public void destroy() {
        System.out.println("LogFilter 종료...");
    }
}