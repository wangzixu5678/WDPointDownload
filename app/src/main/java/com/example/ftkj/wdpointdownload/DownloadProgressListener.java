package com.example.ftkj.wdpointdownload;

/**
 * Created by FTKJ on 2017/8/16.
 */

public interface DownloadProgressListener {
    /**
     *
     * @param read  已经读取长度
     * @param count 总长度
     */
    void update(long read, long count);
    void complete();
}
