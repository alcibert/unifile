package de.vfh.unifile.uf_directory;

import java.util.List;
import org.springframework.stereotype.Service;

/***
 * Hier soll die Arbeit mit den Ordnern passieren.
 * Hier steht die Buisiness Logic
 */
@Service
public class UF_DirectoryService {
    public List<UF_Directory> getDirectorys(){
        return List.of(
            new UF_Directory(
                "Bilder",
                "/"
            ),
            new UF_Directory(
                "Videos",
                "/"
            )
        );
    }
}
