package com.bcuk;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.net.URLClassLoader;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.scope.JobScope;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.ClassUtils;

/**
 * The Class TestConfigurationClassEnhancer.
 */
public class TestConfigurationClassEnhancer {

    /**
     * Test.
     */
    @Test
    public void test() {

        // Running this test with "--add-opens java.base/java.lang=ALL-UNNAMED" parameter, results in the error.

        ClassLoader classLoader = URLClassLoader.newInstance(new URL[0]);

        try (AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext()) {

            applicationContext.setClassLoader(classLoader);
            applicationContext.register(TestConfiguration.class);
            applicationContext.refresh();

            Bean2 bean2 = applicationContext.getBean(Bean2.class);
            Class<?> bean1ReferenceType = bean2.getBean1().getClass();

            //We should get a CGlib-Proxy, because bean1 is in the JobScope
            assertTrue(bean1ReferenceType.getName().contains(ClassUtils.CGLIB_CLASS_SEPARATOR));
            assertTrue(bean1ReferenceType.getSuperclass().equals(Bean1.class));
        }
    }

    /**
     * The Class TestConfiguration.
     */
    @Configuration
    public static class TestConfiguration {

        /**
         * Job scope.
         *
         * @return the job scope
         */
        @Bean
        JobScope jobScope() {
            JobScope jobScope = new JobScope();
            jobScope.setProxyTargetClass(true);
            return jobScope;
        }

        /**
         * Bean 1.
         *
         * @return the bean 1
         */
        @Bean
        @Scope("job")
        Bean1 bean1() {
            return new Bean1();
        }

        /**
         * Bean 2.
         *
         * @return the bean 2
         */
        @Bean
        Bean2 bean2() {
            return new Bean2(bean1());
        }
    }

    /**
     * The Class Bean1.
     */
    public static class Bean1 {
    }

    /**
     * The Class Bean2.
     */
    public static class Bean2 {

        private Bean1 bean1;

        /**
         * Instantiates a new bean 2.
         *
         * @param bean1 the bean 1
         */
        public Bean2(Bean1 bean1) {
            this.bean1 = bean1;
        }

        /**
         * Gets the bean 1.
         *
         * @return the bean 1
         */
        public Bean1 getBean1() {
            return this.bean1;
        }
    }
}