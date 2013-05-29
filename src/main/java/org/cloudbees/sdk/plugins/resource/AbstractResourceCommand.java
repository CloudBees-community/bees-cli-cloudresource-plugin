package org.cloudbees.sdk.plugins.resource;

import com.cloudbees.api.BeesClient;
import com.cloudbees.api.oauth.OauthClient;
import com.cloudbees.sdk.cli.AbstractCommand;
import com.cloudbees.sdk.cli.BeesClientFactory;
import org.kohsuke.args4j.Option;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author Kohsuke Kawaguchi
 */
public abstract class AbstractResourceCommand extends AbstractCommand {
    @Inject
    protected BeesClientFactory factory;

    @Option(name="-a",usage="Specify the account to access")
    String account;

    protected String getAccount() {
        if (account!=null)
            return account;
        return factory.getConfigProperties().getProperty("bees.project.app.domain");
    }

    protected OauthClient createClient() throws IOException {
        return factory.get(BeesClient.class).getOauthClient();
    }
}
