package de.vfh.unifile.uf_directory;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import de.vfh.unifile.uf_directory.UF_DirectoryService;
import de.vfh.unifile.uf_content.UF_Content;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * REST-Controller Klasse zur Steuerung der Ordner.
 */
@CrossOrigin(origins = "http://localhost:5050", maxAge = 3600)
@RestController
@RequestMapping(path = "api/v1.0/directory")
public class UF_DirectoryController {
    
    private final UF_DirectoryService dirService;

    /**
     * Dependecy Injection der Serviceklasse
     * @param dirService die Serviceklasse
     */
    @Autowired
    public UF_DirectoryController(UF_DirectoryService dirService){
        this.dirService = dirService;
    }
    
    /**
     * Listet alle in der Datenbank gespeicherten Directorys auf.
     * @return Liste von UF_Directory Files
     */
    @GetMapping
    public List<UF_Directory> getDirectorys(){
        return dirService.getDirectorys();
    }

    /**
     * Das ist eine Test Klasse zum Kennenlernen von Spring.
     * Sie wird nicht benötigt, sondern war lediglich zum Verstehen von Mappings und paths da.
     * An dieser Test Klasse wurde auch das Prinzip von Unit-Tests erprobt.
     * @return String mit Inhalt "Super Sache"
     */
    @RequestMapping(path = "test")
    @GetMapping
    public String doTest(){
        return "Super Sache";
    }

    /**
     * Listet alle in einem Ordner enthaltenen Inhalte auf.
     * Nicht rekursiv, sondern lediglich der aufgerufene Ordner wird zurückgeliefert.
     * Wird benötigt, damit der File-Explorer die Dateiinhalte anzeigen kann.
     * @param cwd Pfad der abgefragt werden soll.
     * @return UF_Content Abbildung eines Ordners.
     */
    @GetMapping
    @RequestMapping(path = "explore")
    public UF_Content explore(@RequestParam("cwd") String cwd) {
        return dirService.explore(cwd);
    }

    /**
     * Scannt rekursiv den gesamten Inhalt eines angegebenen Ordners.
     * @param volume Volume dem der Inhalt zugewiesen werden soll. Meistens entweder A oder B
     * @param path Pfad, der gescannt werden soll
     * @return UF_Content Abbildung des gescannten Ordners
     * @throws IOException
     */
    @GetMapping
    @RequestMapping(path = "scan/{volume}")
    public UF_Directory scan(@PathVariable String volume, @RequestParam("path") String path) throws IOException{
        return dirService.scanPath(volume, path);
    }
    
    /**
     * Kopiert einen Ordnerinhalt in den anderen. Dabei werden die Präferenzen von Konfliktdateien beachtet.
     * @param sourceVolume Verweis auf das Quellvolumen
     * @param destVolume Verweis auf das Zielvolumen
     */
    @GetMapping("copy/{sourceVolume}/{destVolume}")
    public void copyVolumeContent(@PathVariable String sourceVolume, @PathVariable String destVolume) {
        dirService.copy(sourceVolume, destVolume, "");
        return;
    }
    
}
