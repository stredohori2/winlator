package com.pubg.imobile.xenvironment.components;

import com.pubg.imobile.alsaserver.ALSAClientConnectionHandler;
import com.pubg.imobile.alsaserver.ALSARequestHandler;
import com.pubg.imobile.xconnector.UnixSocketConfig;
import com.pubg.imobile.xconnector.XConnectorEpoll;
import com.pubg.imobile.xenvironment.EnvironmentComponent;

public class ALSAServerComponent extends EnvironmentComponent {
    private XConnectorEpoll connector;
    private final UnixSocketConfig socketConfig;

    public ALSAServerComponent(UnixSocketConfig socketConfig) {
        this.socketConfig = socketConfig;
    }

    @Override
    public void start() {
        if (connector != null) return;
        connector = new XConnectorEpoll(socketConfig, new ALSAClientConnectionHandler(), new ALSARequestHandler());
        connector.setMultithreadedClients(true);
        connector.start();
    }

    @Override
    public void stop() {
        if (connector != null) {
            connector.stop();
            connector = null;
        }
    }
}
