package org.cloudbees.sdk.plugins.resource;

import com.cloudbees.api.cr.BindableSource;
import com.cloudbees.api.cr.BindingCollection;
import com.cloudbees.api.cr.CloudResource;
import com.cloudbees.api.oauth.OauthToken;
import com.cloudbees.api.oauth.TokenRequest;
import com.cloudbees.sdk.cli.BeesCommand;
import com.cloudbees.sdk.cli.CLICommand;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Bind two cloud resources.
 *
 * Still very much a work in progress and experimental.
 *
 * Another purpose of this code is to exercise the {@link CloudResource} client library to
 * make sure its usability.
 *
 * @author Kohsuke Kawaguchi
 */
@BeesCommand(group="Cloud Resource",description="Bind one CR to another")
@CLICommand("cr:bind")
public class BindCommand extends AbstractResourceCommand {
    @Argument(index=0,metaVar="SOURCE_URL",usage="URL of the source cloud resource to bind from",required=true)
    URL source;

    @Argument(index=1,metaVar="SINK_URL",usage="URL of the sink cloud resource to bind to",required=true)
    URL sink;

    @Option(name="-label",usage="Label decorates the binding by describing the kind of binding so that the source can distinguish different kinds of bindings to sinks of similar types")
    String label;

    @Option(name="-S")
    Map<String,String> settings = new HashMap<String, String>();

    @Override
    public int main() throws Exception {
        TokenRequest tr = new TokenRequest()
            .withAccountName(getAccount())
            .withScope("https://api.cloudbees.com/services/api/subscription/read") // HACK for now
            .withScope(source, CloudResource.READ_CAPABILITY)
            .withScope(source, BindingCollection.BIND_CAPABILITY)
            .withScope(sink,CloudResource.READ_CAPABILITY)
            .withGenerateRequestToken(false);
        OauthToken t = createClient().createToken(tr);

        CloudResource source = CloudResource.fromOAuthToken(this.source, t);
        CloudResource sink   = CloudResource.fromOAuthToken(this.sink,   t);

        CloudResource e = source.coerce(BindableSource.class).getBindingCollection().bind(sink, label, settings);
        System.out.println("Created binding: "+e.getUrl());

        return 0;
    }
}
