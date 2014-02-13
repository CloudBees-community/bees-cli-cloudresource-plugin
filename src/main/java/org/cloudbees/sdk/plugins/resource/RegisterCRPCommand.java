package org.cloudbees.sdk.plugins.resource;

import com.cloudbees.api.cr.CloudResource;
import com.cloudbees.api.cr.CloudResourceProvider;
import com.cloudbees.api.cr.CloudResourceProviderRegistry;
import com.cloudbees.api.oauth.OauthToken;
import com.cloudbees.api.oauth.TokenRequest;
import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.net.URL;

/**
 * @author Kohsuke Kawaguchi
 */
@BeesCommand(group="Cloud Resource",description="Register a new CRP")
@CLICommand("cr:register")
public class RegisterCRPCommand extends AbstractResourceCommand {
    @Argument(index=0,metaVar="CRP_URL",usage="URL of the CRP to register",required=true)
    URL crp;

    @Option(name="-registry",usage="CRP registry to register this to")
    URL registry;

    @Override
    public int main() throws Exception {
        if (registry==null)
            registry = new URL("https://resources.cloudbees.com");


        TokenRequest tr = new TokenRequest()
            .withAccountName(getAccount())
            .withScope(registry, CloudResourceProviderRegistry.REGISTER_CAPABILITY)
            .withScope(crp,CloudResource.READ_CAPABILITY)
            .withGenerateRequestToken(false);
        OauthToken t = createClient().createToken(tr);

        CloudResource registry = CloudResource.fromOAuthToken(this.registry, t);
        CloudResource crp      = CloudResource.fromOAuthToken(this.crp,      t);

        registry.coerce(CloudResourceProviderRegistry.class).register(crp.as(CloudResourceProvider.class));

        System.out.println("Registered CRP: " + crp.getUrl());

        return 0;
    }
}
