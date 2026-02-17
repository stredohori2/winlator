package com.pubg.imobile.sysvshm;

import com.pubg.imobile.xconnector.Client;
import com.pubg.imobile.xconnector.RequestHandler;
import com.pubg.imobile.xconnector.XInputStream;
import com.pubg.imobile.xconnector.XOutputStream;
import com.pubg.imobile.xconnector.XStreamLock;

import java.io.IOException;

public class SysVSHMRequestHandler implements RequestHandler {
    @Override
    public boolean handleRequest(Client client) throws IOException {
        SysVSharedMemory sysVSharedMemory = (SysVSharedMemory)client.getTag();
        XInputStream inputStream = client.getInputStream();
        XOutputStream outputStream = client.getOutputStream();

        if (inputStream.available() < 5) return false;
        byte requestCode = inputStream.readByte();

        switch (requestCode) {
            case RequestCodes.SHMGET: {
                long size = inputStream.readUnsignedInt();
                int shmid = sysVSharedMemory.get(size);

                try (XStreamLock lock = outputStream.lock()) {
                    outputStream.writeInt(shmid);
                }
                break;
            }
            case RequestCodes.GET_FD: {
                int shmid = inputStream.readInt();

                try (XStreamLock lock = outputStream.lock()) {
                    outputStream.writeByte((byte)0);
                    outputStream.setAncillaryFd(sysVSharedMemory.getFd(shmid));
                }
                break;
            }
            case RequestCodes.DELETE: {
                int shmid = inputStream.readInt();
                sysVSharedMemory.delete(shmid);
                break;
            }
        }
        return true;
    }
}
