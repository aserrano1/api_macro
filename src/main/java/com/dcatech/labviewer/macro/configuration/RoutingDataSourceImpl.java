package com.dcatech.labviewer.macro.configuration;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RoutingDataSourceImpl extends AbstractRoutingDataSource {
    private String database = "";

    @Override
    protected Object determineCurrentLookupKey() {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            HttpSession session = attr.getRequest().getSession();
            if (session.getAttributeNames().hasMoreElements()) {
                String databaseIntegration = "";
                if (session.getAttribute("databaseIntegration") != null) {
                    databaseIntegration = session.getAttribute("databaseIntegration").toString();
                    return databaseIntegration;
                }

            }
        }
        // String test = auth.getPrincipal().toString();
        if (auth != null && (!auth.isAuthenticated() || (auth instanceof AnonymousAuthenticationToken))) {
            return "dsADM";
        }

        /*if (auth != null && (!auth.isAuthenticated() || (auth instanceof AnonymousAuthenticationToken))) {
            return "dsADM";
        }*/

        if (attr != null) {
            HttpSession session = attr.getRequest().getSession();
            String dataSource = session.getAttribute("database").toString();

            return dataSource.isEmpty() ? null : dataSource;
        }
        return "dsADM";
    }

    public void test(HttpServletRequest request) {
        String bd = request.getSession().getAttribute("bdintegration").toString();
    }


}
