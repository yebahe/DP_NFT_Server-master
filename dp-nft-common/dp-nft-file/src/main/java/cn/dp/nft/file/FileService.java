package cn.dp.nft.file;

import java.io.InputStream;

/**
 * 文件 服务
 *
 * @author yebahe
 */
public interface FileService {

    public boolean upload(String path, InputStream fileStream);

}
