package com.pubg.imobile.xserver.requests;

import static com.pubg.imobile.xserver.XClientRequestHandler.RESPONSE_CODE_SUCCESS;

import com.pubg.imobile.xconnector.XInputStream;
import com.pubg.imobile.xconnector.XOutputStream;
import com.pubg.imobile.xconnector.XStreamLock;
import com.pubg.imobile.xserver.Atom;
import com.pubg.imobile.xserver.XClient;
import com.pubg.imobile.xserver.errors.BadAtom;
import com.pubg.imobile.xserver.errors.XRequestError;

import java.io.IOException;

public abstract class AtomRequests {
    public static void internAtom(XClient client, XInputStream inputStream, XOutputStream outputStream) throws IOException, XRequestError {
        boolean onlyIfExists = client.getRequestData() == 1;
        short length = inputStream.readShort();
        inputStream.skip(2);
        String name = inputStream.readString8(length);
        int id = onlyIfExists ? Atom.getId(name) : Atom.internAtom(name);
        if (id < 0) throw new BadAtom(id);

        try (XStreamLock lock = outputStream.lock()) {
            outputStream.writeByte(RESPONSE_CODE_SUCCESS);
            outputStream.writeByte((byte)0);
            outputStream.writeShort(client.getSequenceNumber());
            outputStream.writeInt(0);
            outputStream.writeInt(id);
            outputStream.writePad(20);
        }
    }
    public static void getAtomName(XClient client, XInputStream inputStream, XOutputStream outputStream) throws IOException, XRequestError {
        int id = inputStream.readInt();
        if (id < 0) throw new BadAtom(id);
        String name = Atom.getName(id);
        short length = (short) name.length();


        try (XStreamLock lock = outputStream.lock()) {
            outputStream.writeByte(RESPONSE_CODE_SUCCESS);
            outputStream.writeByte((byte)0);
            outputStream.writeShort(client.getSequenceNumber());
            outputStream.writeInt((length + 22) / 4);
            outputStream.writeString8(name);
            outputStream.writePad(22);
        }
    }
}
