package de.vfh.unifile.uf_directory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @RequestMapping(path = "explore")
    @GetMapping
    public UF_Content explore(@RequestParam("cwd") String cwd) {
        return dirService.explore(cwd);
    }
    

}
