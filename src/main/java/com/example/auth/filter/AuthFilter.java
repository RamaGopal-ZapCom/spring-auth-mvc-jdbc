package com.example.auth.filter;

import com.example.auth.model.User;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String uri = request.getRequestURI();

        boolean loggedIn = (session != null && session.getAttribute("user") != null);

        // Allow access if request is for login page or static resources
        boolean loginRequest = uri.endsWith("/login") || uri.endsWith("/login/") || uri.endsWith("/login.jsp");
        boolean resourceRequest = uri.contains("/resources/");

        if (!loggedIn && !(loginRequest || resourceRequest)) {
            // Not logged in, redirect to login page
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (loggedIn) {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            String role = user.getRole();

            if (uri.startsWith(request.getContextPath() + "/admin")) {
                if (!"ADMIN".equals(role)) {
                    response.sendRedirect(request.getContextPath() + "/unauthorized");
                    return;
                }
            } else if (uri.startsWith(request.getContextPath() + "/user")) {
                if (!"USER".equals(role) && !"ADMIN".equals(role)) {
                    response.sendRedirect(request.getContextPath() + "/unauthorized");
                    return;
                }
            }
        }

        // If all checks pass, continue filter chain
        chain.doFilter(req, res);
    }


    @Override
    public void init(FilterConfig config) throws ServletException {}

    @Override
    public void destroy() {}
}
