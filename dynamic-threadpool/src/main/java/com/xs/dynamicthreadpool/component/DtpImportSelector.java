package com.xs.dynamicthreadpool.component;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class DtpImportSelector implements DeferredImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{
                DtpImportBeanDefinitionRegistrar.class.getName(),
                DtpBeanPostProcessor.class.getName(),
                DtpMonitor.class.getName()
        };
    }
}