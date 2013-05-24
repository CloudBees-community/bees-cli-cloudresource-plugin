package org.cloudbees.sdk.plugins.resource;

import com.cloudbees.api.BeesClient;
import com.cloudbees.api.oauth.OauthClient;
import com.cloudbees.sdk.cli.AbstractCommand;
import com.cloudbees.sdk.cli.BeesClientFactory;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author Kohsuke Kawaguchi
 */
public abstract class AbstractResourceCommand extends AbstractCommand {
    @Inject
    protected BeesClientFactory factory;

    protected String getDefaultAccount() {
        return factory.getConfigProperties().getProperty("bees.project.app.domain");
    }

    protected OauthClient createClient() throws IOException {
        return factory.get(BeesClient.class).getOauthClient();
    }
}
