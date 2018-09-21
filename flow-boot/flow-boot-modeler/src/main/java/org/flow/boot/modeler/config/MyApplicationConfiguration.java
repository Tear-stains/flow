/* Licensed under the Apache License, Version 2.0 (the "License");*/
package org.flow.boot.modeler.config;

import java.io.IOException;

import org.flowable.ui.modeler.conf.ApplicationConfiguration;
import org.flowable.ui.modeler.conf.SecurityConfiguration;
import org.flowable.ui.modeler.properties.FlowableModelerAppProperties;
import org.flowable.ui.modeler.servlet.ApiDispatcherServletConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
@EnableConfigurationProperties(FlowableModelerAppProperties.class)
@ComponentScan(//
        basePackages = { //
                "org.flowable.ui.modeler.conf", //
                "org.flowable.ui.modeler.repository", //
                "org.flowable.ui.modeler.service", //
                "org.flowable.ui.modeler.security", //
                "org.flowable.ui.common.conf", //
                "org.flowable.ui.common.filter", //
                "org.flowable.ui.common.service", //
                "org.flowable.ui.common.repository", //
                "org.flowable.ui.common.security", //
                "org.flowable.ui.common.tenant" }, //
        excludeFilters = { //
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.class), //
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.ApiWebSecurityConfigurationAdapter.class), //
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.ActuatorWebSecurityConfigurationAdapter.class), //
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfiguration.FormLoginWebSecurityConfigurerAdapter.class), //
                @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ApplicationConfiguration.class) //
        }//
)
public class MyApplicationConfiguration {

    @Bean
    public ServletRegistrationBean<DispatcherServlet> modelerApiServlet(ApplicationContext applicationContext) {
        AnnotationConfigWebApplicationContext dispatcherServletConfiguration = new AnnotationConfigWebApplicationContext();
        dispatcherServletConfiguration.setParent(applicationContext);
        dispatcherServletConfiguration.register(ApiDispatcherServletConfiguration.class);
        DispatcherServlet servlet = new DispatcherServlet(dispatcherServletConfiguration);
        ServletRegistrationBean<DispatcherServlet> registrationBean = new ServletRegistrationBean<>(servlet, "/api/*");
        registrationBean.setName("Flowable Modeler App API Servlet");
        registrationBean.setLoadOnStartup(1);
        registrationBean.setAsyncSupported(true);
        return registrationBean;
    }

}

class TypeExcludeFilter implements TypeFilter {

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
            throws IOException {
        System.err.println(metadataReader);
        System.err.println(metadataReader.getClass());
        System.err.println(metadataReader.getClassMetadata());
        System.err.println(metadataReader.getResource());
        System.err.println();

        // if (this.beanFactory instanceof ListableBeanFactory && getClass() == TypeExcludeFilter.class) {
        // Collection<TypeExcludeFilter> delegates = ((ListableBeanFactory) this.beanFactory)
        // .getBeansOfType(TypeExcludeFilter.class).values();
        // for (TypeExcludeFilter delegate : delegates) {
        // if (delegate.match(metadataReader, metadataReaderFactory)) {
        // return true;
        // }
        // }
        // }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        throw new IllegalStateException("TypeExcludeFilter " + getClass() + " has not implemented equals");
    }

    @Override
    public int hashCode() {
        throw new IllegalStateException("TypeExcludeFilter " + getClass() + " has not implemented hashCode");
    }

}