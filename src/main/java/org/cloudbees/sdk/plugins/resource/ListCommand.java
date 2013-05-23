package org.cloudbees.sdk.plugins.resource;

import com.cloudbees.api.BeesClient;
import com.cloudbees.api.cr.BindableSource;
import com.cloudbees.api.cr.CloudResource;
import com.cloudbees.api.oauth.OauthClient;
import com.cloudbees.sdk.cli.AbstractCommand;
import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;
import org.kohsuke.args4j.Argument;

import java.io.IOException;
import java.net.URL;

/**
 * @author Kohsuke Kawaguchi
 */
@BeesCommand(group="Resource (New)",description="List up all the coud resources from CRP")
@CLICommand("resource2:list")
public class ListCommand extends AbstractCommand {
    @Argument(index=0,metaVar="SOURCE_URL",usage="URL of the source cloud resource to bind from",required=true)
    URL source;

    protected String getDefaultAccount() {
        return factory.getConfigProperties().getProperty("bees.project.app.domain");
    }

    protected OauthClient createClient() throws IOException {
        return factory.get(BeesClient.class).getOauthClient();
    }

    @Override
    public int main() throws Exception {
//        TokenRequest tr = new TokenRequest()
//            .withAccountName(getDefaultAccount())
//            .withScope("https://api.cloudbees.com/services/api/subscription/read") // HACK for now
//            .withScope(source,BindingCollection.BIND_CAPABILITY)
//            .withScope(sink,CloudResource.READ_CAPABILITY)
//            .withGenerateRequestToken(false);
//        OauthToken t = createClient().createToken(tr);


        String oat = "bogus"; // t.accessToken;
        CloudResource source = CloudResource.fromOAuthToken(this.source, oat);
        CloudResource sink   = CloudResource.fromOAuthToken(this.sink,   oat);

        CloudResource e = source.as(BindableSource.class).getBindingCollection().bind(sink, label, settings);
        System.out.println("Created binding: "+e.getUrl());

        return 0;
    }
}
