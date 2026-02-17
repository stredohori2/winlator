package com.pubg.imobile.xenvironment.components;

import com.pubg.imobile.xenvironment.EnvironmentComponent;
import com.pubg.imobile.xconnector.XConnectorEpoll;
import com.pubg.imobile.xconnector.UnixSocketConfig;
import com.pubg.imobile.xserver.XClientConnectionHandler;
import com.pubg.imobile.xserver.XClientRequestHandler;
import com.pubg.imobile.xserver.XServer;

public class XServerComponent extends EnvironmentComponent {
    private XConnectorEpoll connector;
    private final XServer xServer;
    private final UnixSocketConfig socketConfig;

    public XServerComponent(XServer xServer, UnixSocketConfig socketConfig) {
        this.xServer = xServer;
        this.socketConfig = socketConfig;
    }

    @Override
    public void start() {
        if (connector != null) return;
        connector = new XConnectorEpoll(socketConfig, new XClientConnectionHandler(xServer), new XClientRequestHandler());
        connector.setInitialInputBufferCapacity(262144);
        connector.setCanReceiveAncillaryMessages(true);
        connector.start();
    }

    @Override
    public void stop() {
        if (connector != null) {
            connector.stop();
            connector = null;
        }
    }

    public XServer getXServer() {
        return xServer;
    }
}
