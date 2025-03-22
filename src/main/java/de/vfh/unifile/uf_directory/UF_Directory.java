package de.vfh.unifile.uf_directory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
@OnDelete(action = OnDeleteAction.CASCADE)
public class UF_Directory extends UF_Content implements IUF_Directory{
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UF_Content> content = new ArrayList<UF_Content>();

    public UF_Directory(){
        super();
    }

    private UF_Directory(UF_DirectoryBuilder builder) {
        super(builder);
    }
    // public UF_Directory(String name, String relativePath){
    //     super(name, relativePath);
    // }
    // public UF_Directory(String name, Long size, Long lastModified, String absolutePath){
    //     super(name, size, lastModified, absolutePath);
    // }

    /***
     * Buidler Klasse für UF_DirectoryBuilder
     */
    public static class UF_DirectoryBuilder extends UF_Content.UF_ContentBuilder<UF_DirectoryBuilder> {

        @Override
        protected UF_DirectoryBuilder self(){
            return this;
        }

        @Override
        public UF_Directory build() {
            return new UF_Directory(this);
        }
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
                UF_Directory newDir = new UF_Directory.UF_DirectoryBuilder()
                    .setName(child.getName())
                    .setAbsolutePath(child.getAbsolutePath())
                    .setSize(child.length())
                    .setLastModified(child.lastModified())
                    .build();
                newDir.setVolume(this.volume);
                newDir.setRelativePath(MessageFormat.format("{0}\\{1}", this.relativePath, newDir.name));
                newDir.setDirectory(true);
                newDir.setParent(this);
                if (fullDepthScan) {
                    newDir.scanContents();
                }
                this.content.add(newDir);
            }
            if(child.isFile()){
                String hashString = "n.P.";
                try {
                    hashString = getFileHash(child);
                } catch (Exception e) {
                    System.out.println("Could not generate Hash for File " + child.getName());
                }
                UF_File newFile = new UF_File.UF_FileBuilder()
                .setName(child.getName())
                .setSize(child.length())
                .setLastModified(lastModified)
                .setAbsolutePath(child.getAbsolutePath())
                .setHashValue(hashString)
                .build();
                newFile.setVolume(this.volume);
                newFile.setRelativePath(MessageFormat.format("\\{0}", this.relativePath));
                newFile.setDirectory(false);
                newFile.setParent(this);
                this.content.add(newFile);
            }
        }
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

    //ToDo: In File schreiben - nicht in Directory
    private static String getFileHash(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }
        return bytesToHex(digest.digest());
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}

