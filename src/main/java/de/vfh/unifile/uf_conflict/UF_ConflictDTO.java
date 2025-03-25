package de.vfh.unifile.uf_conflict;

import de.vfh.unifile.primitives.MergeStrategy;
import de.vfh.unifile.uf_file.UF_File;

/**
 * Hilfsklasse zum Erstellen richtiger Konflikte.
 * Diese Klasse wird nur benutzt um aus einer eignen Datenbankquery richtige Konflikte zu erstellen.
 */
public class UF_ConflictDTO{
    private Long fileAId;
    private Long fileBId;
    
    public UF_ConflictDTO(){};
    public UF_ConflictDTO(Long fileAId, Long fileBId) {
        this.fileAId = fileAId;
        this.fileBId = fileBId;
    }
    public Long getFileAId() {
        return fileAId;
    }
    public void setFileAId(Long fileAId) {
        this.fileAId = fileAId;
    }
    public Long getFileBId() {
        return fileBId;
    }
    public void setFileBId(Long fileBId) {
        this.fileBId = fileBId;
    }
}