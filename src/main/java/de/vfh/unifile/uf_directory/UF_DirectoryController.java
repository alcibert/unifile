package de.vfh.unifile.uf_directory;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.vfh.unifile.uf_content.UF_Content;
import org.springframework.web.bind.annotation.RequestParam;


/***
 * Hier sollen nur die API Endpoints aufgelistet sein.
 * Alle Aufrufe gehen an den Service Layer
 */
@RestController
@RequestMapping(path = "api/v1.0/directory")
public class UF_DirectoryController {
    
    private final UF_DirectoryService dirService;

    @Autowired
    public UF_DirectoryController(UF_DirectoryService dirService){
        this.dirService = dirService;
    }
    
    @GetMapping
    public List<UF_Content> getDirectorys(){
        return dirService.getDirectorys();
    }

    @RequestMapping(path = "test")
    @GetMapping
    public String doTest(){
        return "Super Sache";
    }

    //ToDo: Die Endpoints in Postman hinterlegen
    @RequestMapping(path = "explore")
    @GetMapping
    public UF_Content explore(@RequestParam("cwd") String cwd) {
        return dirService.explore(cwd);
    }

    //ToDo: Endpoint in Postman hinterlegen
    @RequestMapping(path = "scan/{volume}")
    @GetMapping
    public UF_Directory scan(@PathVariable String volume, @RequestParam("path") String path) throws IOException{
        return dirService.scanPath(volume, path);
    }
    

}
