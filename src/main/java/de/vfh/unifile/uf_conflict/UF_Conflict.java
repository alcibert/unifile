package de.vfh.unifile.uf_conflict;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.vfh.unifile.primitives.MergeStrategy;
import de.vfh.unifile.uf_file.UF_File;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class UF_Conflict {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    @OneToOne
    @JoinColumn(name = "fileA_id")
    private UF_File fileA;
    
    @OneToOne
    @JoinColumn(name = "fileB_id")
    private UF_File fileB;

    @Enumerated(EnumType.STRING)
    private MergeStrategy merge;

    public UF_Conflict(Long id, UF_File fileA, UF_File fileB, MergeStrategy merge) {
        this.id = id;
        this.fileA = fileA;
        this.fileB = fileB;
        this.merge = merge;
    }

    public UF_Conflict(){
        this.merge = MergeStrategy.NOT_SET;
    };

    public MergeStrategy getMerge() {
        return merge;
    }

    public UF_Conflict setMerge(MergeStrategy merge) {
        this.merge = merge;
        return this;
    }

    public UF_File getFileA() {
        return fileA;
    }

    public UF_Conflict setFileA(UF_File fileA) {
        this.fileA = fileA;
        return this;
    }

    public UF_File getFileB() {
        return fileB;
    }

    public void setFileB(UF_File fileB) {
        this.fileB = fileB;
    }

    public Long getId() {
        return id;
    }

}

