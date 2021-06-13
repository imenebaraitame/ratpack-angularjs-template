FROM gitpod/workspace-full

RUN sudo apt-get update

RUN bash -c "./home/gitpod/.sdkman/bin/sdkman-init.sh && sdk install java 8.0.275.hs-adpt"
