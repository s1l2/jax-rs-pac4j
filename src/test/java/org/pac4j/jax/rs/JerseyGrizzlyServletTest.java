package org.pac4j.jax.rs;

import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.util.Globals;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.TestProperties;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.After;
import org.junit.Before;
import org.pac4j.core.config.Config;
import org.pac4j.jax.rs.features.Pac4JSecurityFeature;
import org.pac4j.jax.rs.jersey.features.Pac4JValueFactoryProvider;
import org.pac4j.jax.rs.servlet.features.ServletJaxRsContextFactoryProvider;

/**
 *
 * @author Victor Noel - Linagora
 * @since 1.0.0
 *
 */
public class JerseyGrizzlyServletTest extends AbstractSessionTest {

    private MyJerseyTest jersey;

    @Before
    public void setUp() throws Exception {
        jersey = new MyJerseyTest();
        // let's force use a JerseyClient!
        setUpClientClassloader(JerseyClientBuilder.class);
        jersey.setUp();
    }

    public class MyJerseyTest extends JerseyTest {

        @Override
        protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
            return new GrizzlyWebTestContainerFactory();
        }

        @Override
        protected DeploymentContext configureDeployment() {
            forceSet(TestProperties.CONTAINER_PORT, "0");

            Config config = getConfig();
            ResourceConfig app = new ResourceConfig(getResources())
                    .register(new ServletJaxRsContextFactoryProvider(config))
                    .register(new Pac4JSecurityFeature(config, DEFAULT_CLIENT))
                    .register(new Pac4JValueFactoryProvider.Binder());

            return ServletDeploymentContext.forServlet(new ServletContainer(app)).build();
        }
    }

    @After
    public void tearDown() throws Exception {
        jersey.tearDown();
    }

    @Override
    protected WebTarget getTarget(String url) {
        return jersey.target(url).property(ClientProperties.FOLLOW_REDIRECTS, false);
    }

    @Override
    protected String cookieName() {
        return Globals.SESSION_COOKIE_NAME;
    }
}
