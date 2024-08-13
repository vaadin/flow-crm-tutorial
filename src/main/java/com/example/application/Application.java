package com.example.application;

import java.util.UUID;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.communication.IndexHtmlResponse;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "flowcrmtutorial")
// PWA is not supported when using strict CSP
/*@PWA(
        name = "Vaadin CRM",
        shortName = "CRM",
        offlinePath="offline.html",
        offlineResources = { "images/offline.png" }
)*/
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public VaadinServiceInitListener cspNonceInjector() {
        return initEvent -> initEvent.addIndexHtmlRequestListener(Application::injectCspNonce);
    }

    private static void injectCspNonce(IndexHtmlResponse response) {
        // Use CSP only in production mode
        if (!response.getVaadinRequest().getService().getDeploymentConfiguration().isProductionMode()) {
            return;
        }

        String nonce = UUID.randomUUID().toString();
        VaadinSession.getCurrent().setAttribute("csp-nonce", nonce);

        // Add a header to make the browser require the nonce in all script tags
        response.getVaadinResponse().setHeader("Content-Security-Policy",
                "script-src 'nonce-" + nonce + "'");

        // Add the nonce to all script tags in the host page
        response.getDocument().getElementsByTag("script").attr("nonce", nonce);
    }
}
