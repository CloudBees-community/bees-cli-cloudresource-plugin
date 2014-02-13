package org.cloudbees.sdk.plugins.resource;

import com.cloudbees.api.cr.CloudResource;
import com.cloudbees.api.oauth.OauthToken;
import com.cloudbees.api.oauth.TokenRequest;
import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.kohsuke.args4j.Argument;

import java.net.URL;

/**
 * @author Kohsuke Kawaguchi
 */
@BeesCommand(group="Cloud Resource",description="Obtain the current state of a resource")
@CLICommand("cr:get")
public class GetCommand extends AbstractResourceCommand {
    @Argument(index=0,metaVar="SOURCE_URL",usage="URL of the resource to retrieve",required=true)
    URL source;

    @Override
    public int main() throws Exception {
        TokenRequest tr = new TokenRequest()
            .withAccountName(getAccount())
            .withScope(source, CloudResource.READ_CAPABILITY)
            .withGenerateRequestToken(false);
        OauthToken t = createClient().createToken(tr);

        CloudResource source = CloudResource.fromOAuthToken(this.source, t);

        ObjectMapper om = new ObjectMapper();
        om.enable(Feature.INDENT_OUTPUT);
        om.writeValue(System.out, source.retrieve());

        return 0;
    }
}
