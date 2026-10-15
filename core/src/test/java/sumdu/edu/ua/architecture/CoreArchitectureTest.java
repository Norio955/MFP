package sumdu.edu.ua.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

public class CoreArchitectureTest {

    @Test
    void coreShouldNotDependOnWebOrPersistence() {
        JavaClasses imported = new ClassFileImporter()
                .importPackages("sumdu.edu.ua.core");

        noClasses()
                .that().resideInAPackage("..core..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..web..", "..persistence..")
                .check(imported);
    }

    @Test
    void coreClassesShouldNotUseStandardOutput() {
        JavaClasses imported = new ClassFileImporter()
                .importPackages("sumdu.edu.ua.core");

        NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS
                .check(imported);
    }

    @Test
    void coreClassesShouldResideInCorrectPackage() {
        JavaClasses imported = new ClassFileImporter()
                .importPackages("sumdu.edu.ua.core");

        classes()
                .that().resideInAPackage("..core..")
                .should().resideInAPackage("..core..")
                .allowEmptyShould(true)
                .check(imported);
    }
}