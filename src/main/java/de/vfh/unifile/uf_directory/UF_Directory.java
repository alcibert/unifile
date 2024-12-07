package de.vfh.unifile.uf_directory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.vfh.unifile.uf_content.UF_Content;
import de.vfh.unifile.uf_file.UF_File;

/***
 * Struktur und Funktionen der Objekte landen hier
 */
public class UF_Directory extends UF_Content{
   
    private List<UF_Content> content = new ArrayList<UF_Content>();

    public UF_Directory(){}
    public UF_Directory(String name, String relativePath){
        super(name, relativePath);
    }
    public UF_Directory(String name, Long size, Long lastModified, String relativePath){
        super(name, size, lastModified, relativePath);
    }

    @Override
    public String toString() {
        return "UF_Directory " + super.toString();
    }

    @Override
    public boolean equals(Object other) {
        boolean sameSuper = super.equals(other);
        if (!sameSuper){return false;}
        if (!(other instanceof UF_Directory)) {return false;}
        // ToDo: alle Files durchlaufen und auf gleichheit überprüfen
        return true;
    }

    public void scanContents(){
        File origin = new File(this.relativePath);
        File[] children = origin.listFiles();
        
        for(File child: children){
            if(child.isDirectory()){
                UF_Directory newDir = new UF_Directory(
                    child.getName(),
                    child.length(),
                    child.lastModified(),
                    child.getPath()
                );
                newDir.scanContents();
                this.content.add(newDir);
            }
            if(child.isFile()){
                UF_File newFile = new UF_File(
                    child.getName(),
                    child.length(),
                    child.lastModified(),
                    child.getPath(),
                    child.hashCode()
                );
                this.content.add(newFile);
            }
        }
        System.out.println(this.content);
    }

    public List<UF_Content> getContent() {
        return content;
    }
    
}

