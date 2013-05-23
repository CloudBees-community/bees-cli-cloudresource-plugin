package org.cloudbees.sdk.plugins.resource;

import com.cloudbees.api.BeesClient;
import com.cloudbees.api.cr.BindableSource;
import com.cloudbees.api.cr.CloudResource;
import com.cloudbees.api.oauth.OauthClient;
import com.cloudbees.api.oauth.OauthToken;
import com.cloudbees.api.oauth.TokenRequest;
import com.cloudbees.sdk.cli.AbstractCommand;
import com.cloudbees.sdk.cli.BeesClientFactory;
import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kohsuke Kawaguchi
 */
@BeesCommand(group="Resource (New)",description="Bind one CR to another")
@CLICommand("resource2:bind")
public class BindCommand extends AbstractCommand {
    @Argument(index=0,metaVar="SOURCE_URL",usage="URL of the source cloud resource to bind from",required=true)
    URL source;

    @Argument(index=1,metaVar="SINK_URL",usage="URL of the sink cloud resource to bind to",required=true)
    URL sink;

    @Option(name="-label",usage="Label decorates the binding by describing the kind of binding so that the source can distinguish different kinds of bindings to sinks of similar types")
    String label;

    @Option(name="-S")
    Map<String,String> settings = new HashMap<String, String>();

    @Inject
    protected BeesClientFactory factory;

    protected String getDefaultAccount() {
        return factory.getConfigProperties().getProperty("bees.project.app.domain");
    }

    protected OauthClient createClient() throws IOException {
        return factory.get(BeesClient.class).getOauthClient();
    }

    @Override
    public int main() throws Exception {
        TokenRequest tr = new TokenRequest(null,null,null,getDefaultAccount(),
                "https://api.cloudbees.com/services/api/subscription/read", // HACK for now
                scope(source,"bind"),
                scope(sink,"read")
                );
        OauthToken t = createClient().createToken(tr);

        // TODO: I don't want a refresh token. I don't want a long record of a token

        // TODO: this shall move to the cloudbees-api-client
        CloudResource source = CloudResource.fromOAuthToken(this.source, t.accessToken);
        CloudResource sink   = CloudResource.fromOAuthToken(this.sink,   t.accessToken);

        source.as(BindableSource.class).getBindingCollection().bind(sink, label, settings);

        return 0;
    }

    private String scope(URL source, String capability) {
        String base = source.getHost();
        if (source.getDefaultPort()!=source.getPort()) {
            base += ":"+source.getPort();
        }

        return String.format("crs://%s!%s",base,capability);
    }
}
