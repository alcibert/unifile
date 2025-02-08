package de.vfh.unifile.uf_content;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table
public class UF_Content {
    @Id
    @SequenceGenerator(
        name = "uf_content_sequence",
        sequenceName = "uf_content_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "uf_content_sequence"
    )
    protected Long id;
    protected String name;
    protected Long size;
    protected Long lastModified;
    protected String relativePath;
    protected String absolutePath;
    protected Boolean isDirectory;
    protected String volume;

    public UF_Content(String name, Long size, Long lastModified, String absolutePath) {
        this.name = name;
        this.size = size;
        this.lastModified = lastModified;
        this.absolutePath = absolutePath;
    }

    public UF_Content(

    ){}

    public UF_Content(String name, String absolutePath) {
        this.name = name;
        this.absolutePath = absolutePath;
        // The rest has to be set through the database or after scanning the file system for the file
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public Boolean getIsDirectory() {
        return isDirectory;
    }

    public void setDirectory(Boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

    @Override
    public String toString() {
        return "[id=" + id + ", name=" + name + ", size=" + size + ", lastModified=" + lastModified
                + ", relativePath=" + relativePath + "]";
    }

    @Override
    public boolean equals(Object other) {
        if(other == this){return true;}
        if (!(other instanceof UF_Content)) {return false;}
        
        UF_Content content = (UF_Content) other;
        boolean samePath = this.relativePath.equals(content.getRelativePath());
        boolean sameName = this.name.equals(content.getName());
        boolean sameSize = this.size.equals(content.getSize());
        boolean sameModTime = this.lastModified.equals(content.getLastModified());
        
        return samePath && sameName && sameSize && sameModTime;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }
}

