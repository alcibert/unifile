package de.vfh.unifile.uf_directory;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import de.vfh.unifile.uf_content.UF_Content;
import de.vfh.unifile.uf_file.UF_File;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/***
 * Struktur und Funktionen der Objekte landen hier
 */
@Entity
@Table(name = "UF_Directory")
public class UF_Directory extends UF_Content{
    @Id
    @SequenceGenerator(
        name = "uf_directory_sequence",
        sequenceName = "uf_directory_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "uf_directory_sequence"
    )
    protected Long id;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UF_Content> content = new ArrayList<UF_Content>();

    public UF_Directory(){}
    public UF_Directory(String name, String relativePath){
        super(name, relativePath);
    }
    public UF_Directory(String name, Long size, Long lastModified, String absolutePath){
        super(name, size, lastModified, absolutePath);
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
        scanContents(true);
    }

    public void scanContents(Boolean fullDepthScan){
        File origin = new File(this.absolutePath);
        File[] children = origin.listFiles();
        
        for(File child: children){
            if(child.isDirectory()){
                UF_Directory newDir = new UF_Directory(
                    child.getName(),
                    child.length(),
                    child.lastModified(),
                    child.getAbsolutePath()
                );
                newDir.setVolume(this.volume);
                newDir.setRelativePath(MessageFormat.format("{0}\\{1}", this.relativePath, this.getName()));
                newDir.setDirectory(true);
                newDir.setParent(this);
                if (fullDepthScan) {
                    newDir.scanContents();
                }
                this.content.add(newDir);
            }
            if(child.isFile()){
                UF_File newFile = new UF_File(
                    child.getName(),
                    child.length(),
                    child.lastModified(),
                    child.getAbsolutePath(),
                    child.hashCode()
                );
                newFile.setVolume(this.volume);
                newFile.setRelativePath(MessageFormat.format("{0}\\{1}", this.relativePath, this.getName()));
                newFile.setDirectory(false);
                newFile.setParent(this);
                this.content.add(newFile);
            }
        }
        // System.out.println(this.content);
    }

    public List<UF_Content> getContent() {
        return content;
    }

    public void addToContent(UF_Content toAdd){
        this.content.add(toAdd);
    }
    public void setContent(List<UF_Content> content) {
        this.content = content;
    }
    
}

