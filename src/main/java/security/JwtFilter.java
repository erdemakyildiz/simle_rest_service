package security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtFilter extends GenericFilterBean {

    List<String> allowURL = new ArrayList<>();

    public JwtFilter() {
        allowURL.add("/auth");
    }

    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);

            chain.doFilter(req, res);
        } else {

            if (allowURL.contains(request.getRequestURI())){
                chain.doFilter(req, res);
                return;
            }

            /**
             * jwt token default olarak yerleştirdim. decode zamanının süreye bir etkisi var mı görmek için.
             */
            String authHeader = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlcmRlbSIsInJvbGVzIjoidXNlciIsImlhdCI6MTU1MDg4NDEzM30.MdAXHRZ8LSeWgzNirNKlcrZXDKL8zBfuTFqqJuJuzHs";

            try {
                final Claims claims = Jwts.parser().setSigningKey("secretKey").parseClaimsJws(authHeader).getBody();
                request.setAttribute("claims", claims);
            } catch (final SignatureException e) {
                throw new ServletException("Invalid token");
            }

            chain.doFilter(req, res);
        }
    }
}