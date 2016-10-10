package org.pac4j.jax.rs.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.pac4j.core.config.Config;
import org.pac4j.core.engine.CallbackLogic;
import org.pac4j.core.engine.J2ERenewSessionCallbackLogic;
import org.pac4j.core.util.CommonHelper;

/**
 * 
 * @author Victor Noel - Linagora
 * @since 1.0.0
 *
 */
public class CallbackFilter extends AbstractFilter {

    private CallbackLogic<Object, JaxRsContext> callbackLogic = new J2ERenewSessionCallbackLogic<>();

    private String defaultUrl;

    private Boolean multiProfile;

    private Boolean renewSession;

    public CallbackFilter(HttpServletRequest request, Config config) {
        super(request, config);
    }

    @Override
    protected void filter(JaxRsContext context) throws IOException {
        CommonHelper.assertNotNull("callbackLogic", callbackLogic);

        callbackLogic.perform(context, config, adapter(), context.getAbsolutePath(defaultUrl), multiProfile, renewSession);
    }

    public CallbackLogic<Object, JaxRsContext> getCallbackLogic() {
        return callbackLogic;
    }

    public void setCallbackLogic(CallbackLogic<Object, JaxRsContext> callbackLogic) {
        this.callbackLogic = callbackLogic;
    }

    public String getDefaultUrl() {
        return defaultUrl;
    }

    public void setDefaultUrl(String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    public boolean isMultiProfile() {
        return multiProfile;
    }

    public void setMultiProfile(Boolean multiProfile) {
        this.multiProfile = multiProfile;
    }

    public boolean isRenewSession() {
        return renewSession;
    }

    public void setRenewSession(Boolean renewSession) {
        this.renewSession = renewSession;
    }
}
