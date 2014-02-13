## What is this?

This plugin adds Cloud Resource management commands to [CloudBees SDK](http://wiki.cloudbees.com/bin/view/RUN/BeesSDK) CLI tool.

To install

    $ bees plugin:install org.cloudbees.sdk.plugins:resource-plugin

## List Registered Cloud Resource Providers (CRP)

    $ bees cr:list https://resources.cloudbees.com

Response:

    bees cr:get https://resources.cloudbees.com/5/

## Get a Cloud Resource details

    $ bees cr:get https://resources.cloudbees.com/5/

Response:

    {
      "url" : "https://services-platform.cloudbees.com/api/crp/",
      "types" : [ "https://types.cloudbees.com/resource/provider", "https://types.cloudbees.com/resource" ]
    }

## Register a CRP

    $ bees cr:register YOUR_CRP_URL