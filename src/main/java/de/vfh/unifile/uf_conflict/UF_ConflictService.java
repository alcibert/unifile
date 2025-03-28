package de.vfh.unifile.uf_conflict;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.vfh.unifile.primitives.MergeStrategy;
// import de.vfh.unifile.primitives.MergeStrategy;
import de.vfh.unifile.uf_content.UF_ContentRepository;
// import de.vfh.unifile.uf_file.UF_File;
import de.vfh.unifile.uf_file.UF_File;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;


@Service
public class UF_ConflictService {

    private final UF_ConflictRepository repository;
    private final UF_ContentRepository contentRepository;
    
    /**
     * Dependencyinjection des Datenbankinterfaces der Konflikte und der Contentobjekte
     * @param repository
     * @param contentRepository
     */
    @Autowired
    public UF_ConflictService(UF_ConflictRepository repository, UF_ContentRepository contentRepository) {
        this.repository = repository;
        this.contentRepository = contentRepository;
    }

    @Autowired
    private EntityManager entityManager;

    /**
     * Listet alle Konflikte aus der Datenbank aus
     * @return
     */
    public List<UF_Conflict> getAllConflicts(){
        List<UF_Conflict> found = this.repository.findAllWithFiles();
        for (UF_Conflict conflict : found) {
            Hibernate.initialize(conflict.getFileA());
            Hibernate.initialize(conflict.getFileB());
        }
        return found;
    }

    /**
     * Analysiert die gescanten Files nach Konflikten
     */
    public void analyzeConflicts(){
        this.repository.deleteAll();
        this.repository.flush();
        entityManager.clear();
        List<UF_ConflictDTO> foundConflicts = this.repository.analyzeConflictFiles();
        for (UF_ConflictDTO conflictDTO : foundConflicts) {
            UF_Conflict conf = new UF_Conflict();
            UF_File fA = (UF_File) contentRepository.findById(conflictDTO.getFileAId()).orElse(null);
            UF_File fB = (UF_File) contentRepository.findById(conflictDTO.getFileBId()).orElse(null);
            conf.setFileA(fA);
            conf.setFileB(fB);
            conf.setMerge(MergeStrategy.NOT_SET);
            System.out.println("\n \n \n CONFLIKT: ");
            System.out.println(conf);
            System.out.println("\n \n \n \n");
            this.repository.save(conf);
        }
        System.out.println("\n \n KEINE KONFLIKTE \n \n");
    }

    /**
     * Gibt einen Konflikt mit einer ID zurück
     * @param conflictId Die ID des gesuchten Konflikts
     * @return Der gesuchte Konflikt
     */
    public UF_Conflict getConflict(Long conflictId){
        UF_Conflict conf = this.repository.findById(conflictId).orElse(null);
        return conf;
    }

    /**
     * Setzt die MergeStrategy eines Konflikts
     * @param conflictId Die ID des betroffenen Konflikts
     * @param mergeStrategy Die neue Mergestrategy des Konflikts
     */
    public void setMerge(Long conflictId, MergeStrategy mergeStrategy){
        UF_Conflict conf = this.repository.getReferenceById(conflictId);
        conf.setMerge(mergeStrategy);
        this.repository.save(conf);
    }
}
