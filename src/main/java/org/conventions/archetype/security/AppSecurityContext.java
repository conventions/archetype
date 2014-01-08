package org.conventions.archetype.security;

import org.conventions.archetype.event.UpdateUserRoles;
import org.conventions.archetype.model.User;
import org.conventions.archetype.service.UserService;
import org.conventionsframework.exception.BusinessException;
import org.conventionsframework.qualifier.Config;
import org.conventionsframework.qualifier.Log;
import org.conventionsframework.qualifier.LoggedIn;
import org.conventionsframework.security.DefaultSecurityContext;
import org.conventionsframework.util.MessagesController;
import org.conventionsframework.util.ResourceBundle;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Specializes;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.net.URLDecoder;
import java.util.List;
import java.util.logging.Logger;

@SessionScoped
@Specializes
@Named("security")
public class AppSecurityContext extends DefaultSecurityContext implements Serializable {

    protected User user;
    private List<String> userRolesCache;//avoid quering user roles on each permission check
    private String username;
    private String password;
    private String pageRecovery;
    @Inject
    @Config
    transient Instance<ExternalContext> externalContext;
    @Inject
    @Config
    transient Instance<FacesContext> facesContext;
    @Inject
    @Config
    transient Instance<HttpServletRequest> request;
    @Inject
    UserService userService;
    @Inject
    ResourceBundle bundle;
    @Inject
    @Log
    transient Logger log;

    @Produces
    @LoggedIn
    @SessionScoped
    public User getUser() {
        return user == null ? new User():user;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPageRecovery() {
        return pageRecovery;
    }

    public void setPageRecovery(String pageRecovery) {
        this.pageRecovery = pageRecovery;
    }

    public void doLogon() {
        user = userService.findUser(username, password);
        if (user == null) {
            throw new BusinessException(bundle.getString("logon.be.incorrect"));
        }
        userRolesCache = user.getUserRoles();
        restorePageOnLogon();
        MessagesController.addInfo(bundle.getString("logon.info.successful"));
    }

    private void restorePageOnLogon() {
        ExternalContext ec = externalContext.get();
        if (getPageRecovery() != null) {
            try {
                ec.redirect(ec.getRequestContextPath() + URLDecoder.decode(pageRecovery.replaceAll(".xhtml", ".faces"), "UTF-8"));
                MessagesController.addInfo(bundle.getString("logon.info.successful"));
                facesContext.get().responseComplete();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                setPageRecovery(null);
            }

        }
        /**
         * above also works String referer =
         * ec.getRequestHeaderMap().get("referer"); if
         * (referer.contains("?page")) { try {
         * ec.redirect(ec.getRequestContextPath() +
         * pageRecovery.replaceAll(".xhtml",".faces")); //also works //String
         * decodedReferer = URLDecoder.decode(referer, "UTF-8");
         * //ec.redirect(ec.getRequestContextPath() +
         * decodedReferer.substring(decodedReferer.indexOf("page=") +
         * 5).replaceAll("xhtml", "faces")); } catch (Exception e) {
         * e.printStackTrace(); }finally { setPageRecovery(null); } }
         */
    }



    public void updateUserRolesEvent(@Observes UpdateUserRoles updateUserRoles) {
        userRolesCache = updateUserRoles.getUserRoles();
    }

    public void init() {
        if (!facesContext.get().isPostback()) {
            if (getPageRecovery() != null) {
                MessagesController.addWarn(bundle.getString("expire.message"));
            }
        }
    }

    //tells conventions whether user is logged or not
    @Override
    @Produces
    @LoggedIn
    @Named
    public Boolean loggedIn() {
        log.info("logged in:"+(user != null && user.getId() != null));
        return user != null && user.getId() != null;
    }

    @Override
    public Boolean hasAnyRole(String... roleName) {
        for (String r : roleName) {
            if(user != null && userRolesCache != null && userRolesCache.contains(r)){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean hasRole(String role){
        return hasAnyRole(role);
    }

    /** @Override
     @Produces
     @LoggedIn
     @Named public Boolean loggedIn() {
     return super.loggedIn();
     } */
}