package de.vfh.unifile.uf_directory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.vfh.unifile.primitives.MergeStrategy;
import de.vfh.unifile.uf_conflict.UF_Conflict;
import de.vfh.unifile.uf_conflict.UF_ConflictRepository;
import de.vfh.unifile.uf_content.UF_Content;
import de.vfh.unifile.uf_file.UF_File;
import de.vfh.unifile.uf_content.UF_ContentRepository;

/***
 * Hier soll die Arbeit mit den Ordnern passieren.
 * Hier steht die Buisiness Logic
 */
@Service
public class UF_DirectoryService {

    private final UF_DirectoryRepository repository;
    private final UF_ContentRepository contentRepository;
    private final UF_ConflictRepository conflictRepository;
    
    @Autowired
    public UF_DirectoryService(UF_DirectoryRepository repository, UF_ContentRepository contentRepository, UF_ConflictRepository conflictRepository) {
        this.repository = repository;
        this.contentRepository = contentRepository;
        this.conflictRepository = conflictRepository;
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
        List<UF_Content> existing = this.repository.findByVolume(volume);
        for(UF_Content found : existing){
            this.contentRepository.delete(found);
        }
        File origin = new File(path);
        UF_Directory newDir = new UF_Directory(
            origin.getName(),
            0L,
            origin.lastModified(),
            origin.getAbsolutePath()
        );
        if(!volume.equals("none")){
            newDir.setVolume(volume.toLowerCase());
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
            // newDrive.setAbsolutePath("");
            newDrive.setDirectory(true);
            newDir.addToContent(newDrive);
        }
        return newDir;
    }

    public List<UF_Content> getAllFiles(String volume){
        return this.repository.findByVolume(volume);
    }

    public void copy(String sourceVolume, String destVolume, String sourceRelPath){
        UF_Directory source = this.repository.getRootDir(sourceVolume, sourceRelPath);
        UF_Directory destination = this.repository.getRootDir(destVolume, "");

        for (UF_Content element : source.getContent()){
            if(element.getIsDirectory()){
                //Recursively Loop through the Directorys
                System.out.println("Recursive Call started");
                copy(sourceVolume, destVolume, element.getRelativePath());
                System.out.println("Recursive Call ended");
                continue;
            }
            UF_File sourceFile = (UF_File) element;
            String destinationPath = destination.getAbsolutePath() + sourceFile.getRelativePath() + '/' + sourceFile.getName();
            System.out.println("Call to copy");
            
            UF_Conflict conflict = this.conflictRepository.findByFileId(element.getId());
            if (conflict == null){
                sourceFile.copyTo(destinationPath);
                continue;
            }
            System.out.println("Conflict Found with Strategy: " + conflict.getMerge());
            if (conflict.getMerge() == MergeStrategy.IGNORE){ continue;}
            if (conflict.getMerge() == MergeStrategy.KEEP_A){}
            if (conflict.getMerge() == MergeStrategy.KEEP_B){
                sourceFile = conflict.getFileB();
                destinationPath = source.getAbsolutePath() + sourceFile.getRelativePath() + '/' + sourceFile.getName();
            }
            if (conflict.getMerge() == MergeStrategy.NOT_SET){ continue;}
            sourceFile.copyTo(destinationPath);
        }
    }
}

