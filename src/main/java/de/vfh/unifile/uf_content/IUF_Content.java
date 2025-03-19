package de.vfh.unifile.uf_content;

import de.vfh.unifile.uf_directory.UF_Directory;

public interface IUF_Content{

    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    Long getSize();

    Long getLastModified();

    String getRelativePath();

    Boolean getIsDirectory();

    void setDirectory(Boolean isDirectory);

    String getAbsolutePath();

    void setAbsolutePath(String absolutePath);

    String getVolume();

    void setVolume(String volume);

    void setRelativePath(String relativePath);

    void setParent(UF_Directory parent);
}