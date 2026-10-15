package sumdu.edu.ua.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

public class PersistenceArchitectureTest {

    @Test
    void persistenceShouldNotDependOnWeb() {
        JavaClasses imported = new ClassFileImporter()
                .importPackages("sumdu.edu.ua.persistence");

        noClasses()
                .that().resideInAPackage("..persistence..")
                .should().dependOnClassesThat()
                .resideInAPackage("..web..")
                .check(imported);
    }

    @Test
    void persistenceClassesShouldNotUseStandardOutput() {
        JavaClasses imported = new ClassFileImporter()
                .importPackages("sumdu.edu.ua.persistence");

        NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS
                .check(imported);
    }

    @Test
    void persistenceClassesShouldResideInCorrectPackage() {
        JavaClasses imported = new ClassFileImporter()
                .importPackages("sumdu.edu.ua.persistence");

        classes()
                .that().resideInAPackage("..persistence..")
                .should().resideInAPackage("..persistence..")
                .allowEmptyShould(true)
                .check(imported);
    }
}