package de.vfh.unifile.uf_directory;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import de.vfh.unifile.primitives.MergeStrategy;
import de.vfh.unifile.uf_conflict.*;
import de.vfh.unifile.uf_content.*;


@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
class UF_DirectoryServiceTest {
    @Autowired
    private UF_DirectoryService directoryService;
    @Autowired
    private UF_DirectoryRepository directoryRepository;
    @Autowired
    private UF_ConflictRepository conflictRepository;
    @Autowired
    private UF_ContentRepository contentRepository;
    @Autowired
    private UF_ConflictService conflictService;
    @Autowired
    private EntityManager entityManager;


    private List<UF_Conflict> conflicts;

    private static final String A_VOLUME = "src/test/java/de/vfh/unifile/test_A";
    private static final String B_VOLUME = "src/test/java/de/vfh/unifile/test_B";
    private static final String FILE_NAME = "testfile.txt";


    @BeforeEach
    void setUp() throws IOException {
        contentRepository = Mockito.mock(UF_ContentRepository.class);
        directoryRepository = Mockito.mock(UF_DirectoryRepository.class);
        conflictRepository = Mockito.mock(UF_ConflictRepository.class);
        entityManager = Mockito.mock(EntityManager.class);
        
        // Service mit Mock-Repositorys initialisieren
        // directoryService = new UF_DirectoryService(directoryRepository, contentRepository, conflictRepository);
        // conflictService = new UF_ConflictService(conflictRepository, contentRepository);
        

        // Testordner anlegen
        Files.createDirectories(Paths.get(A_VOLUME));
        Files.createDirectories(Paths.get(B_VOLUME));

        // Testdateien anlegen
        Files.writeString(Paths.get(A_VOLUME, FILE_NAME), "Inhalt A");
        Files.writeString(Paths.get(B_VOLUME, FILE_NAME), "Inhalt B");

        directoryService.scanPath("a", A_VOLUME);
        directoryService.scanPath("b", B_VOLUME);

        conflictService.analyzeConflicts();
        conflicts = conflictService.getAllConflicts();
    }

    @AfterEach
    void tearDown() throws IOException {
        // Lösche alle Dateien und Ordner nach dem Test
        Files.walk(Paths.get(A_VOLUME)).map(Path::toFile).forEach(File::delete);
        Files.walk(Paths.get(B_VOLUME)).map(Path::toFile).forEach(File::delete);
    }

    @Test
    void testCopy_MergeStrategy_IGNORE() {
        for (UF_Conflict conflict : conflicts) { conflict.setMerge(MergeStrategy.IGNORE);}
        directoryService.copy("a","b", "");
        String contentA = readFile(A_VOLUME, FILE_NAME);
        String contentB = readFile(B_VOLUME, FILE_NAME);
        assertEquals("Inhalt A", contentA, "Keine Datei sollte überschrieben werden");
        assertEquals("Inhalt B", contentB, "Keine Datei sollte überschrieben werden");
    }

    @Test
    void testCopy_MergeStrategy_KEEP_A() {
        for (UF_Conflict conflict : conflicts) { conflict.setMerge(MergeStrategy.KEEP_A);}
        directoryService.copy("a","b", "");
        String contentA = readFile(A_VOLUME, FILE_NAME);
        String contentB = readFile(B_VOLUME, FILE_NAME);
        assertEquals("Inhalt A", contentA, "Datei A soll B überschreiben");
        assertEquals("Inhalt A", contentB, "Datei A soll B überschreiben");
    }

    @Test
    void testCopy_MergeStrategy_KEEP_B() {
        for (UF_Conflict conflict : conflicts) { conflict.setMerge(MergeStrategy.KEEP_B);}
        directoryService.copy("b","a", "");
        String contentA = readFile(A_VOLUME, FILE_NAME);
        String contentB = readFile(B_VOLUME, FILE_NAME);
        assertEquals("Inhalt B", contentA, "Datei B soll A überschreiben");
        assertEquals("Inhalt B", contentB, "Datei B soll A überschreiben");
    }

    private String readFile(String volume, String filename) {
        try {
            return Files.readString(Paths.get(volume, filename));
        } catch (IOException e) {
            fail("Fehler beim Lesen der Datei");
            return null;
        }
    }

    @Test
    void testGetDirectorys() {
        
    }
}