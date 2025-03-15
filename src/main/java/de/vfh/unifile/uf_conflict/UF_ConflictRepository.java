package de.vfh.unifile.uf_conflict;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UF_ConflictRepository extends JpaRepository<UF_Conflict, Long> {

    @Query(value = """
        SELECT new de.vfh.unifile.uf_conflict.UF_ConflictDTO(a.id, b.id)
        FROM 
        (SELECT a.id AS id, a.name AS name, a.relativePath AS relativePath, a.size AS size, a.isDirectory AS isDirectory, a.volume AS volume, b.id AS fileAId, b.hashValue AS hashValue
        FROM UF_Content a
        LEFT JOIN UF_File b ON a.id = b.id
        WHERE a.isDirectory = FALSE AND a.volume = 'a') a
        INNER JOIN
        (SELECT a.id AS id, a.name AS name, a.relativePath AS relativePath, a.size AS size, a.isDirectory AS isDirectory, a.volume AS volume, b.id AS fileBId, b.hashValue AS hashValue
        FROM UF_Content a
        LEFT JOIN UF_File b ON a.id = b.id
        WHERE a.isDirectory = FALSE AND a.volume = 'b') b
        ON a.name = b.name 
        AND a.relativePath = b.relativePath
        AND NOT a.hashValue = b.hashValue
        """)
    List<UF_ConflictDTO> analyzeConflictFiles();
}

