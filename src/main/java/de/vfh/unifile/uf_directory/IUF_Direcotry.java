package de.vfh.unifile.uf_directory;

import java.util.List;

import de.vfh.unifile.uf_content.IUF_Content;
import de.vfh.unifile.uf_content.UF_Content;

public interface IUF_Direcotry extends IUF_Content{
    
    void scanContents();

    void scanContents(Boolean fullDepthScan);

    List<UF_Content> getContent();

    void addToContent(UF_Content toAdd);

    void setContent(List<UF_Content> content);
    
}
