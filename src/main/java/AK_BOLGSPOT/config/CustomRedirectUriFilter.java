
package AK_BOLGSPOT.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomRedirectUriFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Only act on the Google authorization request
        if (request.getRequestURI().equals("/oauth2/authorization/google")) {
            String redirectUri = request.getParameter("redirect_uri");
            if (redirectUri != null && !redirectUri.isEmpty()) {
                HttpSession session = request.getSession();
                // Save the intended return URL in the session
                session.setAttribute("postLoginRedirectUrl", redirectUri);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
