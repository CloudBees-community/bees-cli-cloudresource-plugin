package org.cloudbees.sdk.plugins.resource;

import com.cloudbees.api.cr.CloudResource;
import com.cloudbees.api.cr.CloudResourceProvider;
import com.cloudbees.api.oauth.OauthToken;
import com.cloudbees.api.oauth.TokenRequest;
import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;
import org.kohsuke.args4j.Argument;

import java.net.URL;

/**
 * @author Kohsuke Kawaguchi
 */
@BeesCommand(group="Resource (New)",description="List up all the cloud resources from CRP")
@CLICommand("resource2:list")
public class ListCommand extends AbstractResourceCommand {
    @Argument(index=0,metaVar="SOURCE_URL",usage="URL of a cloud resource provider",required=true)
    URL source;

    @Override
    public int main() throws Exception {
        TokenRequest tr = new TokenRequest()
            .withAccountName(getDefaultAccount())
            .withScope(source, CloudResource.READ_CAPABILITY)
            .withGenerateRequestToken(false);
        OauthToken t = createClient().createToken(tr);

        CloudResource source = CloudResource.fromOAuthToken(this.source, t);

        for (CloudResource res : source.as(CloudResourceProvider.class)) {
            System.out.println(res);
        }

        return 0;
    }
}
