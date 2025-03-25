package de.vfh.unifile.uf_file;

import java.io.File;
import java.io.IOException;
// import org.apache.commons.io.FileUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static java.nio.file.StandardCopyOption.*;
import static java.nio.file.LinkOption.*;

import de.vfh.unifile.uf_content.UF_Content;
import de.vfh.unifile.uf_directory.UF_Directory;
import de.vfh.unifile.uf_directory.UF_Directory.UF_DirectoryBuilder;
import jakarta.persistence.Entity;

@Entity
@OnDelete(action = OnDeleteAction.CASCADE)
public class UF_File extends UF_Content implements IUF_File{

    private String hashValue;

    public String getHashValue() {
        return hashValue;
    }

    public UF_File(){
        super();
    }

    private UF_File(UF_FileBuilder builder) {
        super(builder);
        this.id = builder.id;
        this.hashValue = builder.hashValue;
    }
    // public UF_File(String name, String absolutePath){
    //     super(name, absolutePath);
    // }
    // public UF_File(String name, Long size, Long lastModified, String absolutePath, String hashValue){
    //     super(name, size, lastModified, absolutePath);
    //     this.hashValue = hashValue;
    // }
    // public UF_File(Long id, String name, Long size, Long lastModified, String absolutePath, String hashValue){
    //     this(name, size, lastModified, absolutePath, hashValue);
    //     this.id = id;
    // }

    /***
     * Buidler Klasse für UF_FileBuilder
     */
    public static class UF_FileBuilder extends UF_Content.UF_ContentBuilder<UF_FileBuilder> {
        private Long id;
        private String hashValue;

        public UF_FileBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public UF_FileBuilder setHashValue(String hashValue) {
            this.hashValue = hashValue;
            return this;
        }

        @Override
        protected UF_FileBuilder self(){
            return this;
        }

        @Override
        public UF_File build() {
            return new UF_File(this);
        }
    }

    //     private Long id;
    //     private String name;
    //     private Long size = 0L;
    //     private Long lastModified = 0L;
    //     private String absolutePath;
    //     private String hashValue = "";

    //     public UF_FileBuilder() {
    //     }

    //     public UF_FileBuilder setName(String name) {
    //         this.name = name;
    //         return this;
    //     }

    //     public UF_FileBuilder setabsolutePath(String absolutePath) {
    //         this.absolutePath = absolutePath;
    //         return this;
    //     }

    //     public UF_FileBuilder setId(Long id) {
    //         this.id = id;
    //         return this;
    //     }

    //     public UF_FileBuilder setSize(Long size) {
    //         this.size = size;
    //         return this;
    //     }

    //     public UF_FileBuilder setLastModified(Long lastModified) {
    //         this.lastModified = lastModified;
    //         return this;
    //     }

    //     public UF_FileBuilder setHashValue(String hashValue) {
    //         this.hashValue = hashValue;
    //         return this;
    //     }

    //     public UF_File build() {
    //             return new UF_File(this);
    //     }
    // }

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
