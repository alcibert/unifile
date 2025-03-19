package de.vfh.unifile.uf_file;

import de.vfh.unifile.uf_content.IUF_Content;

public interface IUF_File extends IUF_Content{
    
    String getHashValue();

    void copyTo(String destinationPath);
}
