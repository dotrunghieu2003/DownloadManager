package com.sample.server;

import java.io.File;
import java.util.List;

public interface IDownloadHandler {

    /**
     *  htpps://aws/...public....
     *
     * @param db_url_source
     * @param fieName
     * @return
     */
    public File getFile( String fieName);

    public List<String> listFile();
}
