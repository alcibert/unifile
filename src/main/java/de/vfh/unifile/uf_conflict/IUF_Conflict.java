package de.vfh.unifile.uf_conflict;

import de.vfh.unifile.primitives.MergeStrategy;
import de.vfh.unifile.uf_file.UF_File;

public interface IUF_Conflict {
    
    MergeStrategy getMerge();

    UF_Conflict setMerge(MergeStrategy merge);

    UF_File getFileA();

    UF_Conflict setFileA(UF_File file);

    UF_File getFileB();

    void setFileB(UF_File file);

    Long getId();
}
