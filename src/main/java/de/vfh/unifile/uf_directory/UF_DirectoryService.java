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
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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
    
    /**
     * Dependencyinjection der notwendigen Datenbankinterfaces
     * @param repository Directory Repository
     * @param contentRepository Content Repository
     * @param conflictRepository Conflict Repository
     */
    @Autowired
    public UF_DirectoryService(UF_DirectoryRepository repository, UF_ContentRepository contentRepository, UF_ConflictRepository conflictRepository) {
        this.repository = repository;
        this.contentRepository = contentRepository;
        this.conflictRepository = conflictRepository;
    }

    @Autowired
    private EntityManager entityManager;

    /**
     * Gibt alle gefundenen Ordner zurück
     * @return Liste aller gefudnenen Ordner als UF_Directory Objekt.
     */
    public List<UF_Directory> getDirectorys(){
        return this.repository.findAll();
    }

    /**
     * Gibt nur einen Ordner zurück, der sich am angegebenen Pfad befindet.
     * Wenn als Pfad nur "." angegeben ist, liefert diese Funktion statt dem Installationspfad, alle verfügbaren Datenträger aus.
     * @param cwd Pfad, dessen Inhalt angezeigt werden soll
     * @return UF_Content Objekt des angeforderten Pfades
     */
    public UF_Content explore(String cwd){
        if(cwd.equals(".")){
            return listConnectedDrives();
        }
        return scanContent(cwd, false);
    }

    /**
     * Scannt vorhandene Pfade rekursiv und gibt den Inhalt als Ordner Objekt zurück
     * @param volume Volumenbezeichner
     * @param path Pfad der gescannt werden soll
     * @return UF_Directory Objekt des gescannten Ordners
     * @throws IOException
     */
    public UF_Directory scanPath(String volume, String path) throws IOException{
        return scanContent(path,true, volume);
    }

    /**
     * Überladung von scanContent.
     * Leitet weiter an scanContent(String, Boolean, String)
     * @param path Pfad, der gescannt werden soll
     * @param fullDepthScan Boolean ob rekursiv der gesamte Ordnerinhalt gescannt werden soll oder nur die erste Ebene.
     * @return UF_Directory Objekt des gescannten Ordners
     */
    private UF_Directory scanContent(String path, Boolean fullDepthScan){
        return scanContent(path, fullDepthScan, "none");
    }

    /**
     * Scannt einen Ordner auf dem Filesystem und legt mit dem gefundenen Inhalt die UF_File und UF_Directory Objekte an.
     * @param path Pfad der gescannt werden soll
     * @param fullDepthScan Boolean ob rekursiv alles gescannt werden soll oder nur die erste Ebene
     * @param volume Volumenbezeichners des gescannten Pfades
     * @return UF_Directory Objects des gescannten Ordners
     */
    private UF_Directory scanContent(String path, Boolean fullDepthScan, String volume){
        UF_Directory root = this.repository.getRootDir(volume.toLowerCase(), "");
        if(root != null){
            this.conflictRepository.deleteAll();
            this.conflictRepository.flush();
            this.repository.delete(root);
            this.repository.flush();
            this.contentRepository.flush();
            entityManager.clear();
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
        if(fullDepthScan){
            repository.save(newDir);
        }
        return newDir;
    }
    
    /**
     * Fragt im Host-System die angeschlossenen Laufwerke ab.
     * @return UF_Directory Objekt mit allen verfügbaren Laufwerken als Content.
     */
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

    /**
     * Gibt alle gefundenen Files aus einem Volumen zurück
     * @param volume Volumenbezeichner
     * @return Liste von UF_Content Objekten
     */
    public List<UF_Content> getAllFiles(String volume){
        return this.repository.findByVolume(volume);
    }

    /**
     * Kopiert die Files von einem Ort an den anderen. Dabei werden die Präferenzen der Konfliktfiles beachtet.
     * Die Funktion ist Rekursiv.
     * @param sourceVolume Quellvolumen
     * @param destVolume Zielvolumen
     * @param sourceRelPath Relativer Pfad der Quelle. Bei initialem Aufruf: "".
     */
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

