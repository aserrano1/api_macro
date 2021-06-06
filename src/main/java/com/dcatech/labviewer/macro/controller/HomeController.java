package com.dcatech.labviewer.macro.controller;

import com.dcatech.labviewer.macro.repository.LVQueryRepository;
import com.dcatech.labviewer.macro.model.Argument;
import com.dcatech.labviewer.macro.model.LVUser;
import com.dcatech.labviewer.macro.model.ULabviewerconnectEntity;
import com.dcatech.labviewer.macro.repository.LVQueryRepository;
import com.dcatech.labviewer.macro.repository.LVUserRepository;
import com.dcatech.labviewer.macro.repository.LabviewerConnectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Created by LuiFer on 23/06/2017.
 */
@Controller
public class HomeController {

    @Autowired
    LVQueryRepository lvQueryRepository;

    @Autowired
    private SessionRegistry sessionRegistry;


    @Autowired
    LVUserRepository lvUserRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    LabviewerConnectRepository labviewerConnectRepository;

    @RequestMapping(value = {"/popUpWin"}, method = RequestMethod.GET)
    String popUpWinPage(ModelMap model) {
        return "pages/popUpWin";
    }

    @RequestMapping(value = {"/", "/home", "/login"}, method = RequestMethod.GET)
    String loginPage(ModelMap model, @CookieValue(value = "database", defaultValue = "") String databaseCookie) {
        //getActiveSessions();
        logger.info("intento de conexión...");
        if (getUsuario() != null) {
            return "pages/index";
        } else {
            List<String> dataSources = lvUserRepository.getDataSources();
            model.addAttribute("allDataSources", dataSources);
            model.addAttribute("databaseCookie", databaseCookie);
            return "pages/login/login2";
        }
    }


    @RequestMapping(value = "/viewactivesessions", method = RequestMethod.GET)
    String viewActiveSessions(ModelMap model) {
        logger.info("Ver sesiones activas...");
        return "pages/admin/activesessions";
    }

    ///getactiveusers

    @RequestMapping(value = {"/samples"}, method = RequestMethod.GET)
    String index(ModelMap model) {
        LVUser user = HomeController.getUsuario();
        if (user != null) {
            model.addAttribute("userName", user.getUsername());

        }
        return "pages/SampleQueryPage";
    }

    @RequestMapping(value = {"/results", "/results/{sampleId}"}, method = RequestMethod.GET)
    String page2(ModelMap model, @PathVariable("sampleId") Optional<String> sampleId) {
        if (sampleId.isPresent()) {
            model.addAttribute("sampleId", sampleId.get());
        } else {
            model.addAttribute("sampleId", "");
        }
        return "pages/SampleResultPage";
    }

    @RequestMapping(value = {"/query"}, method = RequestMethod.GET)
    String query(ModelMap model,
                 @QueryParam("loadParameters")
                 @DefaultValue("false")
                         boolean loadQuery,
                 HttpServletRequest request) {
        LVUser user = HomeController.getUsuario();
        logger.info("intento de loadQuery del usuario " + user.getEmail());
        if (loadQuery) {
            model.addAttribute("selectedQuery", request.getSession().getAttribute("selectedQuery"));
        }
        return "pages/queries/QueriesLab";
    }

//    @RequestMapping(value = {"/error"}, method = RequestMethod.GET)
//    String errorPage(ModelMap model,
//                 @QueryParam("loadParameters")
//                 @DefaultValue("false")
//                         boolean loadQuery,
//                 HttpServletRequest request) {
//        LVUser user = HomeController.getUsuario();
//        logger.info("intento de loadQuery del usuario " + user.getEmail());
//        if (loadQuery) {
//            model.addAttribute("selectedQuery", request.getSession().getAttribute("selectedQuery"));
//        }
//        return "pages/queries/QueriesLab";
//    }

    public static int countActiveSession;
    //private List<SessionInformation> activeSessions;

    /*public List<SessionInformation> getActiveSessions() {
        activeSessions = new ArrayList<>();
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            activeSessions.addAll(sessionRegistry.getAllSessions(principal, false));
        }
        countActiveSession = activeSessions.size();
<<<<<<< HEAD
        /*TreeCache tree = null;
        try {
            tree = new TreeCache();
            tree.setClusterName("../resources/config/jboss-cache-clustered.xml");
            tree.setClusterProperties("jboss"); // uses defaults if not provided
            tree.setCacheMode(TreeCache.REPL_SYNC);
            //tree.createService(); // not necessary, but is same as MBean lifecycle
            tree.startService(); // kick start tree cache
            tree.put("/a/b/c", "name", "Ben");
            tree.put("/a/b/c/d", "uid", new Integer(322649));
            Integer tmp = (Integer) tree.get("/a/b/c/d", "uid");
            tree.remove("/a/b");
            tree.stopService();
            tree.destroyService(); // not necessary, but is same as MBean lifecycle
        } catch (Exception e) {
            e.printStackTrace();
        }

        return activeSessions;

    }*/

    public String getActiveSessions() {
        List<ULabviewerconnectEntity> listUsersConnection = labviewerConnectRepository.getUserConnections();

        countActiveSession = listUsersConnection.size();
        return "";
    }


    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @Transactional
    public String indexweb() {
        getActiveSessions();
        // countActiveSession();
        return "/pages/index";
    }


    @RequestMapping(value = "/error", method = RequestMethod.GET)
    @Transactional
    public String error() {
        LVUser user = HomeController.getUsuario();
        logger.info("ingreso al index..." + user.getEmail());
        return "/pages/index";
    }

    public static LVUser getUsuario() {

        LVUser user = null;
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof LVUser) {
                user = (LVUser) principal;
            } else {
                return null;
            }
        }
        return user;
    }

    public static List<Argument> getUserArguments() {
        LVUser user = HomeController.getUsuario();
        List<Argument> arguments = new ArrayList<>();
        Argument username = new Argument();
        Argument logonname = new Argument();
        username.setTitle("Usuario");
        username.setName("[SYSUSERID]");
        username.setValue(user.getSysUserId());
        logonname.setTitle("Usuario");
        logonname.setName("[LOGON_NAME]");
        logonname.setValue(user.getLogonName());
        arguments.add(username);
        arguments.add(logonname);
        return arguments;
    }

    @RequestMapping(value = {"/profile"}, method = RequestMethod.GET)
    String profile(ModelMap model) {
        return "pages/profile/Profile";
    }

    @RequestMapping(value = {"/historicos"}, method = RequestMethod.GET)
    String historicos(ModelMap model) {
        return "templates/HistoricMenu";
    }

}
