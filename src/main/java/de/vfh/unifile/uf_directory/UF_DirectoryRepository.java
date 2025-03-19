package de.vfh.unifile.uf_directory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import de.vfh.unifile.uf_content.UF_Content;
import jakarta.transaction.Transactional;

public interface UF_DirectoryRepository extends JpaRepository<UF_Directory, Long>{

    @Query("SELECT s FROM UF_Content s WHERE s.volume = :volume")
    List<UF_Content> findByVolume(@Param("volume") String volume);
}
