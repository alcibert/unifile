package de.vfh.unifile.uf_action;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;

import de.vfh.unifile.uf_directory.UF_Directory;

@Service
public class UF_ActionService {
    public UF_Directory scanPath(String path) throws IOException{
        File origin = new File(path);
        if(!origin.isDirectory()){
            //throw new Error
        }
        UF_Directory newDir = new UF_Directory(
            origin.getName(),
            0L,
            origin.lastModified(),
            origin.getPath()
        );
        newDir.scanContents();
        return newDir;
    }
}
