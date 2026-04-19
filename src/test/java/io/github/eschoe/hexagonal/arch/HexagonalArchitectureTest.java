package io.github.eschoe.hexagonal.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class HexagonalArchitectureTest {

    private static final String ROOT = "io.github.eschoe.hexagonal";
    private static JavaClasses classes;

    @BeforeAll
    static void importClasses() {
        classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(ROOT);
    }

    @Test
    void domainMustNotDependOnApplicationOrAdapter() {
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAnyPackage("..application..", "..adapter..")
            .check(classes);
    }

    @Test
    void applicationMustNotDependOnAdapter() {
        noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAPackage("..adapter..")
            .check(classes);
    }

    @Test
    void domainMustNotDependOnSpring() {
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("org.springframework..")
            .check(classes);
    }

    @Test
    void servicesMustLiveInApplicationService() {
        noClasses()
            .that().haveSimpleNameEndingWith("Service")
            .and().resideInAPackage(ROOT + "..")
            .should().resideOutsideOfPackage("..application.service..")
            .check(classes);
    }

    @Test
    void controllersMustLiveInAdapterInWeb() {
        noClasses()
            .that().haveSimpleNameEndingWith("Controller")
            .and().resideInAPackage(ROOT + "..")
            .should().resideOutsideOfPackage("..adapter.in.web..")
            .check(classes);
    }

    @Test
    void persistenceAdaptersMustLiveInAdapterOutPersistence() {
        noClasses()
            .that().haveSimpleNameEndingWith("PersistenceAdapter")
            .and().resideInAPackage(ROOT + "..")
            .should().resideOutsideOfPackage("..adapter.out.persistence..")
            .check(classes);
    }

    @Test
    void layeredBoundaryFromContexts() {
        Architectures.layeredArchitecture().consideringOnlyDependenciesInLayers()
            .layer("Domain").definedBy("..domain..")
            .layer("Application").definedBy("..application..")
            .layer("Adapter").definedBy("..adapter..")
            .whereLayer("Adapter").mayNotBeAccessedByAnyLayer()
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Adapter")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Adapter")
            .check(classes);
    }
}
