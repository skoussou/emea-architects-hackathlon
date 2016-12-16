# Red Hat EMEA Architects PAAS Hackathlon

Hackathlon Presentation: http://redhat.slides.com/atarocch/arch-summit#/
* [Local Copy of Hackathlon instructions](docs/arch-summit.pdf)

### The Hackathlon
The main aim is to bring an application (MSA) on OCP and collaborate with other MSAs and DevOps teams.

### Team Format
We will work on teams of 2 in order to match people with diverse skillset and meet colleagues from across the organisation. Teams will be defined in the spreadsheet: *TBD*
### Hackathlon Prep Guide

In the spreadsheet there is an empty column "Rocket Channel & bluejeans link",this is for you to decide on how to communicate as a team during the hackathlon.

Pre-requisites
* Install oc tools (>= v.1.3) [1]
* You will have already been assigned a namespace/project listed in the spreadsheet above accessible via oc tools or opensift console (https://*TBD*:8443)
* Login {username}/{username}123 (right now you will not have access so don't worry)
* OCP DNS is not working so you will have to set the IP (TBD) against any service you need to access in your /etc/hosts or change the route name with *appname.35.156.180.17.xip.io* or *.nip.io*
* You will probably need a repository on github/gitlab for your application between the team

### The services
* proxy-api : starts the calls to other services and orchestrates the calls
* alabaster-snowball : One of the services handling payload and reading ENV Variables (WILDFLY-SWARM)
* bushy-evergreen : One of the services handling payload and reading ENV Variables (EAP, NODEJS)
* shinny-upatree : One of the services handling payload and reading ENV Variables (springboot)
* wunorse-openslae : A copy of the alabaster-snowball

## Setting Up the Hackathlon Environment

* [Install AWS OCP Environment](docs/aws_ocp_installation.adoc)
* [Setup Hackathlon Environment Apps, Configs etc.](docs/Steps-For-Hackathlon-Apps-Setup.adoc)

## Testing

* A [json file](docs/insomnia-hackathlon-api.json) with insomnia REST app is available to test (change the IPs)

