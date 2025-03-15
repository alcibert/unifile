package de.vfh.unifile.uf_conflict;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.vfh.unifile.primitives.MergeStrategy;
// import de.vfh.unifile.primitives.MergeStrategy;
import de.vfh.unifile.uf_content.UF_ContentRepository;
// import de.vfh.unifile.uf_file.UF_File;
import de.vfh.unifile.uf_file.UF_File;


@Service
public class UF_ConflictService {

    private final UF_ConflictRepository repository;
    private final UF_ContentRepository contentRepository;
    
    @Autowired
    public UF_ConflictService(UF_ConflictRepository repository, UF_ContentRepository contentRepository) {
        this.repository = repository;
        this.contentRepository = contentRepository;
    }

    public List<UF_Conflict> getAllConflicts(){
        return this.repository.findAll();
    }

    public void analyzeConflicts(){
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
    
    public UF_Conflict getConflict(Long conflictId){
        return this.repository.getReferenceById(conflictId);
    }

    public void updateConflict(UF_Conflict conf){
        this.repository.save(conf);
    }
}
