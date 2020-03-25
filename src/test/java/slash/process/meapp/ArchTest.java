package slash.process.meapp;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("slash.process.meapp");

        noClasses()
            .that()
                .resideInAnyPackage("slash.process.meapp.service..")
            .or()
                .resideInAnyPackage("slash.process.meapp.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..slash.process.meapp.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
