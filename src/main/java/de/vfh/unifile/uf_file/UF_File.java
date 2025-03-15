package de.vfh.unifile.uf_file;

import de.vfh.unifile.uf_content.UF_Content;
import jakarta.persistence.Entity;

@Entity
public class UF_File extends UF_Content{

    private String hashValue;

    public String getHashValue() {
        return hashValue;
    }
    public UF_File(){};
    public UF_File(String name, String absolutePath){
        super(name, absolutePath);
    }
    public UF_File(String name, Long size, Long lastModified, String absolutePath, String hashValue){
        super(name, size, lastModified, absolutePath);
        this.hashValue = hashValue;
    }
    public UF_File(Long id, String name, Long size, Long lastModified, String absolutePath, String hashValue){
        this(name, size, lastModified, absolutePath, hashValue);
        this.id = id;
    }

    @Override
    public String toString() {
        return "UF_File [hashValue=" + this.hashValue + "]" + super.toString();
    }
}
