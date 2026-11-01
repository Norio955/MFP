package sumdu.edu.ua.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class WebArchitectureTest {

    @Test
    void webShouldNotDependOnPersistenceDirectly() {
        JavaClasses imported = new ClassFileImporter()
                .importPackages("sumdu.edu.ua");

        noClasses()
                .that().resideInAPackage("..web..")
                .should().dependOnClassesThat()
                .resideInAPackage("..persistence..")
                .check(imported);
    }

    @Test
    void servletsShouldResideInWebPackage() {
        classes()
                .that().haveSimpleNameEndingWith("Servlet")
                .should().resideInAPackage("..web..")
                .allowEmptyShould(true) // Дозволяємо порожню вибірку (сервлетів немає)
                .check(new ClassFileImporter().importPackages("sumdu.edu.ua"));
    }

    @Test
    void repositoriesShouldResideInPersistencePackage() {
        classes()
                .that().haveSimpleNameEndingWith("Repository")
                .should().resideInAPackage("..persistence..")
                .allowEmptyShould(true)
                .check(new ClassFileImporter().importPackages("sumdu.edu.ua"));
    }
}