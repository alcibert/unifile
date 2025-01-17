package de.vfh.unifile.uf_directory;

import java.io.File;
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

    public UF_Content explore(String cwd){
        if(cwd.equals(".")){
            // Give a List of all available Drives
            File[] paths;
            paths = File.listRoots();
            UF_Directory newDir = new UF_Directory("Available Drives",".");
            newDir.setAbsolutePath("");
            for(File path:paths){
                UF_Directory newDrive = new UF_Directory(path.toString(), path.toString());
                newDrive.setAbsolutePath("");
                newDrive.setDirectory(true);
                newDir.addToContent(newDrive);
            }
            return newDir;
        }

        File origin = new File(cwd);
        Boolean isDirectory = origin.isDirectory();
        UF_Directory newDir = new UF_Directory(
            origin.getName(),
            0L,
            origin.lastModified(),
            origin.getPath()
        );
        newDir.setAbsolutePath(origin.getAbsolutePath());
        newDir.setDirectory(isDirectory);
        newDir.scanContents(false);
        return newDir;
    }
}
