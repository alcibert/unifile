package de.vfh.unifile.uf_directory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.vfh.unifile.uf_content.UF_Content;
import de.vfh.unifile.uf_directory.UF_DirectoryRepository;

/***
 * Hier soll die Arbeit mit den Ordnern passieren.
 * Hier steht die Buisiness Logic
 */
@Service
public class UF_DirectoryService {
    private final UF_DirectoryRepository repository;
    
    @Autowired
    public UF_DirectoryService(UF_DirectoryRepository repository) {
        this.repository = repository;
    }

    public List<UF_Directory> getDirectorys(){
        return this.repository.findAll();
    }

    public UF_Content explore(String cwd){
        if(cwd.equals(".")){
            return listConnectedDrives();
        }
        return scanContent(cwd, false);
    }

    public UF_Directory scanPath(String volume, String path) throws IOException{
        return scanContent(path,true, volume);
    }

    private UF_Directory scanContent(String path, Boolean fullDepthScan){
        return scanContent(path, fullDepthScan, "none");
    }

    private UF_Directory scanContent(String path, Boolean fullDepthScan, String volume){
        File origin = new File(path);
        UF_Directory newDir = new UF_Directory(
            origin.getName(),
            0L,
            origin.lastModified(),
            origin.getAbsolutePath()
        );
        if(!volume.equals("none")){
            newDir.setVolume(volume);
        }
        newDir.setRelativePath("");
        newDir.setDirectory(origin.isDirectory());
        newDir.scanContents(fullDepthScan);
        repository.save(newDir);
        return newDir;
    }
    
    private UF_Directory listConnectedDrives(){
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
}
