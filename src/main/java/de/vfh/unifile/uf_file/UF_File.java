package de.vfh.unifile.uf_file;

import java.io.File;
import java.io.IOException;
// import org.apache.commons.io.FileUtils;
import java.nio.file.Files;
import java.nio.file.Path;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static java.nio.file.StandardCopyOption.*;
import static java.nio.file.LinkOption.*;

import de.vfh.unifile.uf_content.UF_Content;
import jakarta.persistence.Entity;

@Entity
@OnDelete(action = OnDeleteAction.CASCADE)
public class UF_File extends UF_Content implements IUF_File{

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

    public void copyTo(String destinationPath){
        System.out.println("Trying to copy");
        try {
            Path bytes = Files.copy(
                new File(this.absolutePath).toPath(),
                new File(destinationPath).toPath(),
                REPLACE_EXISTING, COPY_ATTRIBUTES, NOFOLLOW_LINKS
            );
        } catch (IOException e){
            System.out.println("Copy Failed, Stack Trace");
            e.printStackTrace();
            System.out.println("Copy Failed, End of Stack Trace");
        }
    }
}
