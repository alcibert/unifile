package de.vfh.unifile.uf_conflict;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UF_ConflictRepository extends JpaRepository<UF_Conflict, Long> {

    @Query(value = """
        SELECT new de.vfh.unifile.uf_conflict.UF_ConflictDTO(a.id, b.id)
        FROM 
        (SELECT a.id as id, a.name as name, a.relativePath as relativePath, a.size as size, a.isDirectory as isDirectory, a.volume as volume, b.id as fileAId, b.hashValue as hashValue
        FROM UF_Content a
        LEFT JOIN UF_File b ON a.id = b.id
        WHERE a.isDirectory = FALSE AND a.volume = 'A') a
        INNER JOIN
        (SELECT a.id as id, a.name as name, a.relativePath as relativePath, a.size as size, a.isDirectory as isDirectory, a.volume as volume, b.id as fileBId, b.hashValue as hashValue
        FROM UF_Content a
        LEFT JOIN UF_File b ON a.id = b.id
        WHERE a.isDirectory = FALSE AND a.volume = 'B') b
        ON a.name = b.name 
        AND a.relativePath = b.relativePath
        AND NOT a.hashValue = b.hashValue
        """, nativeQuery = true)
    List<UF_ConflictDTO> analyzeConflictFiles();
}