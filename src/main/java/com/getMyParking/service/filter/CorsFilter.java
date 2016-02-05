package com.getMyParking.service.filter;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by karan on 13/10/15.
 */
@Singleton
public class CorsFilter implements Filter{

    private Session session;

    public CorsFilter(Session session) {
        this.session = session;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        if(response instanceof HttpServletResponse){
            HttpServletResponse alteredResponse = ((HttpServletResponse)response);
            addCorsHeader(alteredResponse);
            if(this.session!=null){
                if(this.session.isOpen()){
                    this.session.close();
                }
                System.out.println("closing session if existed");
            }
        }

        filterChain.doFilter(request, response);
    }

    private void addCorsHeader(HttpServletResponse response){
        //TODO: externalize the Allow-Origin
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept,GMP_AUTH");
        response.addHeader("Access-Control-Max-Age", "1728000");
    }

    @Override
    public void destroy() {}

    @Override
    public void init(FilterConfig filterConfig)throws ServletException {}
}