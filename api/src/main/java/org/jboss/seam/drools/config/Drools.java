/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.drools.config;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.jboss.seam.drools.qualifiers.config.DefaultConfig;
import org.jboss.seam.solder.bean.generic.GenericConfiguration;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Drools configuration file.
 *
 * @author Tihomir Surdilovic
 * @author Stuart Douglas
 */
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER, TYPE})
@GenericConfiguration(DefaultConfig.class)
public @interface Drools {
    public String name() default "";

    public String kbuilderConfigFile() default "";

    public String kbaseConfigFile() default "";

    public String ksessionConfigFile() default "";

    public String kagentConfigFile() default "";

    public String serializationSigningConfigFile() default "";

    public boolean startChangeNotifierService() default false;

    public boolean startChangeScannerService() default false;

    public int scannerInterval() default -1;

    public String agentName() default "";

    public String loggerName() default "";

    public String loggerType() default "";

    public String loggerPath() default "";

    public int loggerInterval() default 0;

    public boolean disableSeamDelegate() default false;

    public DroolsProperty[] kbuilderProperties() default {};

    public DroolsProperty[] kbaseProperties() default {};

    public DroolsProperty[] ksessionProperties() default {};

    public DroolsProperty[] kagentPropertiest() default {};

    public DroolsProperty[] serializationSigningProperties() default {};

}
