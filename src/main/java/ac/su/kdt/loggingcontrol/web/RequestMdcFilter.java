package ac.su.kdt.loggingcontrol.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RequestMdcFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest req, jakarta.servlet.http.HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        try {
            MDC.put("user_id",   nullToDash(req.getParameter("user-id")));
            MDC.put("tx_id",     nullToDash(req.getParameter("tr-id")));
            MDC.put("client_ip", firstIp(req.getHeader("X-Forwarded-For"), req.getRemoteAddr()));
            MDC.put("user_agent", req.getHeader("User-Agent"));
            MDC.put("referer",    req.getHeader("Referer"));
            chain.doFilter(req, res);
        } finally {
            MDC.clear();
        }
    }
    private String nullToDash(String v){ return (v==null||v.isBlank())? "-" : v; }
    private String firstIp(String xff, String remote){
        if (xff!=null && !xff.isBlank()) return xff.split(",")[0].trim();
        return remote;
    }
}

