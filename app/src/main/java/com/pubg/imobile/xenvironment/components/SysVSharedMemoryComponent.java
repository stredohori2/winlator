package com.pubg.imobile.xenvironment.components;

import com.pubg.imobile.sysvshm.SysVSHMConnectionHandler;
import com.pubg.imobile.sysvshm.SysVSHMRequestHandler;
import com.pubg.imobile.sysvshm.SysVSharedMemory;
import com.pubg.imobile.xconnector.UnixSocketConfig;
import com.pubg.imobile.xconnector.XConnectorEpoll;
import com.pubg.imobile.xenvironment.EnvironmentComponent;
import com.pubg.imobile.xserver.SHMSegmentManager;
import com.pubg.imobile.xserver.XServer;

public class SysVSharedMemoryComponent extends EnvironmentComponent {
    private XConnectorEpoll connector;
    public final UnixSocketConfig socketConfig;
    private SysVSharedMemory sysVSharedMemory;
    private final XServer xServer;

    public SysVSharedMemoryComponent(XServer xServer, UnixSocketConfig socketConfig) {
        this.xServer = xServer;
        this.socketConfig = socketConfig;
    }

    @Override
    public void start() {
        if (connector != null) return;
        sysVSharedMemory = new SysVSharedMemory();
        connector = new XConnectorEpoll(socketConfig, new SysVSHMConnectionHandler(sysVSharedMemory), new SysVSHMRequestHandler());
        connector.start();

        xServer.setSHMSegmentManager(new SHMSegmentManager(sysVSharedMemory));
    }

    @Override
    public void stop() {
        if (connector != null) {
            connector.stop();
            connector = null;
        }

        sysVSharedMemory.deleteAll();
    }
}
