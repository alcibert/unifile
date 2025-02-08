package de.vfh.unifile.uf_file;

import de.vfh.unifile.uf_content.UF_Content;

public class UF_File extends UF_Content{
    private int hashValue;

    public int getHashValue() {
        return hashValue;
    }
    public UF_File(){};
    public UF_File(String name, String absolutePath){
        super(name, absolutePath);
    }
    public UF_File(String name, Long size, Long lastModified, String absolutePath, int hashValue){
        super(name, size, lastModified, absolutePath);
        this.hashValue = hashValue;
    }

    @Override
    public String toString() {
        return "UF_File [hashValue=" + this.hashValue + "]" + super.toString();
    }
}
