package de.vfh.unifile.uf_conflict;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(path = "api/v1.0/conflict")
public class UF_ConflictController {
    private final UF_ConflictService conflictService;

    @Autowired
    public UF_ConflictController(UF_ConflictService conflictService){
        this.conflictService = conflictService;
    }

    @GetMapping()
    public List<UF_Conflict> getAllConflicts() {
        return this.conflictService.getAllConflicts();
    }

    @GetMapping("analyze")
    public List<UF_Conflict> analyzeConflicts() {
        // ToDo: Wenn Liste nicht leer, dann erst alle löschen und dann neu anlegen
        this.conflictService.analyzeConflicts();
        return this.conflictService.getAllConflicts();
    }
    
}
