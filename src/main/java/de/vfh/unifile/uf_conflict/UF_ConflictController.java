package de.vfh.unifile.uf_conflict;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.vfh.unifile.primitives.MergeStrategy;
import de.vfh.unifile.uf_conflict.UF_ConflictService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;



@CrossOrigin(origins = "http://localhost:5050", maxAge = 3600)
@RestController
@RequestMapping(path = "api/v1.0/conflict")
public class UF_ConflictController {
    private final UF_ConflictService conflictService;

    /**
     * Dependecyinjection der Serviceklasse
     * @param conflictService
     */
    @Autowired
    public UF_ConflictController(UF_ConflictService conflictService){
        this.conflictService = conflictService;
    }

    /**
     * Gibt alle gefundenen Konflikte zurück
     * @return Liste der gefundenen Konflikte
     */
    @GetMapping()
    public List<UF_Conflict> getAllConflicts() {
        return this.conflictService.getAllConflicts();
    }

    /**
     * Analysiert die gescannten Dateien auf Konflikte
     * @return Liste der gefundenen Konflikte
     */
    @GetMapping("analyze")
    public List<UF_Conflict> analyzeConflicts() {
        // ToDo: Wenn Liste nicht leer, dann erst alle löschen und dann neu anlegen
        this.conflictService.analyzeConflicts();
        return this.conflictService.getAllConflicts();
    }

    /**
     * Setzt bei einem Konflikt die Mergestrategy
     * @param conflictId Konfliktid die aktualisiert werden soll
     * @param mergeStrategy Der neue Wert der Mergestrategy
     * @return Gibt bei Erfolg die neu gesetzte Merge Strategy zurück
     */
    @PostMapping("{conflictId}/setMergeStrategy")
    public MergeStrategy setMergeStrategy(@PathVariable Long conflictId, @RequestBody MergeStrategy mergeStrategy) {
        this.conflictService.setMerge(conflictId, mergeStrategy);
        UF_Conflict conf = this.conflictService.getConflict(conflictId);
        return conf.getMerge();
    }
    
}
