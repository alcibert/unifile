package de.vfh.unifile.uf_directory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import de.vfh.unifile.uf_content.UF_Content;
import jakarta.transaction.Transactional;

public interface UF_DirectoryRepository extends JpaRepository<UF_Directory, Long>{

    /**
     * Gibt alle Inhalte eines gesannten Ordners aufgrund des angegebenen Volumenbezeichners zurück.
     * @param volume Volumenbezeichner
     * @return Liste von UF_Content Elementen
     */
    @Query("SELECT s FROM UF_Content s WHERE s.volume = :volume")
    List<UF_Content> findByVolume(@Param("volume") String volume);

    /**
     * Gibt den Ordner als Objekt zurück, der als Ursprung zu einer Datei gefunden wurde ist.
     * @param volume Volumenbezeichner
     * @param relativePath Relativer Pfad des Ordners
     * @return UF_Directory Objekt des Ordners
     */
    @Query("SELECT s FROM UF_Content s WHERE s.volume = :volume AND s.relativePath = :relPath")
    UF_Directory getRootDir(@Param("volume") String volume, @Param("relPath") String relativePath);
}
