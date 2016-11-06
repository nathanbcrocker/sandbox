package com.digiup.sandbox.config;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

@Configuration
public class DroolsAutoConfiguration {

    @Value("${drools.rulepath")
    private String rulesPath;

    private static final String DEFAULT_RULES_PATH = "rules/";

    @Bean
    public KieFileSystem kieFileSystem() throws IOException {
        String path = rulesPath == null ? DEFAULT_RULES_PATH : rulesPath;
        KieFileSystem kieFileSystem = getKieServices().newKieFileSystem();
        for (Resource file : getRuleFiles()) {
            kieFileSystem.write(ResourceFactory.newClassPathResource(path + file.getFilename(), "UTF-8"));
        }
        return kieFileSystem;
    }

    private Resource[] getRuleFiles() throws IOException {
        String path = rulesPath == null ? DEFAULT_RULES_PATH : rulesPath;
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        return resourcePatternResolver.getResources("classpath*:" + path + "**/*.*");
    }

    @Bean
    public KieContainer kieContainer() throws IOException {
        final KieRepository kieRepository = getKieServices().getRepository();

        kieRepository.addKieModule(new KieModule() {
            public ReleaseId getReleaseId() {
                return kieRepository.getDefaultReleaseId();
            }
        });

        KieBuilder kieBuilder = getKieServices().newKieBuilder(kieFileSystem());
        kieBuilder.buildAll();

        return getKieServices().newKieContainer(kieRepository.getDefaultReleaseId());
    }

    private KieServices getKieServices() {
        return KieServices.Factory.get();
    }

    @Bean
    public KieBase kieBase() throws IOException {
        return kieContainer().getKieBase();
    }

    @Bean
    public KieSession kieSession() throws IOException {
        return kieContainer().newKieSession();
    }
}


