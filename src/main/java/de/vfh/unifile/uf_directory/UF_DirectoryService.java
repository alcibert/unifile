package de.vfh.unifile.uf_directory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.vfh.unifile.uf_content.UF_Content;
import de.vfh.unifile.uf_content.UF_ContentRepository;

/***
 * Hier soll die Arbeit mit den Ordnern passieren.
 * Hier steht die Buisiness Logic
 */
@Service
public class UF_DirectoryService {
    private final UF_ContentRepository repository;
    
    @Autowired
    public UF_DirectoryService(UF_ContentRepository repository) {
        this.repository = repository;
    }

    public List<UF_Content> getDirectorys(){
        return this.repository.findAll();
    }
}
