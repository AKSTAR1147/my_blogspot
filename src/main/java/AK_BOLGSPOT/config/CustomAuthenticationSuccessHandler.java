package AK_BOLGSPOT.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        HttpSession session = request.getSession();

        // 1. Check the session for our saved URL
        String redirectUrl = (String) session.getAttribute("postLoginRedirectUrl");

        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            // 2. Important: Clean up the session attribute
            session.removeAttribute("postLoginRedirectUrl");
            // 3. Redirect to the saved URL
            response.sendRedirect(redirectUrl);
        } else {
            // 4. If nothing was saved, redirect to the homepage
            response.sendRedirect("/");
        }
    }
}