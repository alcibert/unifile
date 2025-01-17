package de.vfh.unifile.uf_action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.vfh.unifile.uf_directory.UF_Directory;
import java.io.IOException;

@RestController
@RequestMapping(path = "api/v1.0/action")
public class UF_ActionController {
    private final UF_ActionService actionService;

    @Autowired
    public UF_ActionController(UF_ActionService actionService){
        this.actionService = actionService;
    }

    @RequestMapping(path = "scan/{volume}")
    @GetMapping
    public UF_Directory scan(@PathVariable String volume, @RequestParam("path") String path) throws IOException{
        return actionService.scanPath(volume, path);
    }

}
