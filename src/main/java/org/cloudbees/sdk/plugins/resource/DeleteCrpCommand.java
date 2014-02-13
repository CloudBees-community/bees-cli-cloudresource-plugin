package org.cloudbees.sdk.plugins.resource;

import com.cloudbees.api.cr.CloudResource;
import com.cloudbees.api.cr.CloudResourceProviderRegistry;
import com.cloudbees.api.oauth.OauthToken;
import com.cloudbees.api.oauth.TokenRequest;
import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;
import org.kohsuke.args4j.Argument;

import java.net.URL;

/**
 * @author Vivek Pandey
 */
@BeesCommand(group="Cloud Resource",description="Delete CRP from registry")
@CLICommand("cr:delete")
public class DeleteCrpCommand extends AbstractResourceCommand {
    @Argument(index=0,metaVar="CRP_URL",usage="URL of the CRP registry to delete",required=true)
    URL registry;

    @Override
    public int main() throws Exception {
        if (registry==null)
            registry = new URL("https://resources.cloudbees.com");


        TokenRequest tr = new TokenRequest()
                .withAccountName(getAccount())
                .withScope(registry, CloudResourceProviderRegistry.REGISTER_CAPABILITY)
                .withGenerateRequestToken(false);
        OauthToken t = createClient().createToken(tr);

        CloudResource registry = CloudResource.fromOAuthToken(this.registry, t);

        registry.coerce(CloudResourceProviderRegistry.class).delete();

        System.out.println("Deleted CRP: " + registry.getUrl());

        return 0;
    }
}
