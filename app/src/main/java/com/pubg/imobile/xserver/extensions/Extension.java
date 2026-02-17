package com.pubg.imobile.xserver.extensions;

import com.pubg.imobile.xconnector.XInputStream;
import com.pubg.imobile.xconnector.XOutputStream;
import com.pubg.imobile.xserver.XClient;
import com.pubg.imobile.xserver.errors.XRequestError;

import java.io.IOException;

public interface Extension {
    String getName();

    byte getMajorOpcode();

    byte getFirstErrorId();

    byte getFirstEventId();

    void handleRequest(XClient client, XInputStream inputStream, XOutputStream outputStream) throws IOException, XRequestError;
}
