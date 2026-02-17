package com.pubg.imobile.core;

import java.io.File;

public interface OnExtractFileListener {
    File onExtractFile(File destination, long size);
}
