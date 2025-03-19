package de.vfh.unifile.uf_file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "api/v1.0/file")
public class UF_FileController {
    private final UF_FileService fileService;

    /**
     * Dependencyinjection der File Service Klasse
     * @param fileService
     */
    @Autowired
    public UF_FileController(UF_FileService fileService){
        this.fileService = fileService;
    }
    
}
