package com.example.application;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import jakarta.servlet.http.HttpServlet;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

import javax.imageio.ImageIO;
import java.util.Set;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
@NpmPackage(value = "lumo-css-framework", version = "^4.0.10")
@Theme("flowcrmtutorial")
//@PWA(name = "VaadinCRM", shortName = "CRM", offlinePath="offline.html", offlineResources = { "./images/offline.png"})
//@ImportRuntimeHints(Application.Hints.class)
public class Application implements AppShellConfigurator {

    static class Hints implements RuntimeHintsRegistrar {
        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            var mcs = MemberCategory.values();

            for (var c : Set.of(ImageIO.class, MainLayout.class)) {
                hints.reflection().registerType(c, mcs);
            }
            for (var r : Set.of("*default-logo.png", "./images/offline.png")) {
                hints.resources().registerPattern(r);
            }
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
