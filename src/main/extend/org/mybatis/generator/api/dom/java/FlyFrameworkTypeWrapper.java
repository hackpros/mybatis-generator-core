/*
 * Copyright 2006 The Apache Software Foundation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mybatis.generator.api.dom.java;

import java.text.MessageFormat;

import org.mybatis.generator.api.IntrospectedTable;

/**
 * @author fly
 * 
 */
public class FlyFrameworkTypeWrapper extends FullyQualifiedJavaType
{

    private static FlyFrameworkTypeWrapper basicLongInstance;

    private static FlyFrameworkTypeWrapper frameworkPagesInstance;

    private static FlyFrameworkTypeWrapper springAnnotationPropagation;

    private static FlyFrameworkTypeWrapper springAnnotationTransactionalInstance;

    private static FlyFrameworkTypeWrapper frameworkBaseSericeInstance;
    
    private static FlyFrameworkTypeWrapper frameworkBaseControlInstance;
    
    private static FlyFrameworkTypeWrapper frameworkBaseMappleInstance;

    private String toPrimitiveMethod;

    /**
     * Use the static getXXXInstance methods to gain access to one of the type
     * wrappers.
     * 
     * @param fullyQualifiedName
     *            fully qualified name of the wrapper type
     * @param toPrimitiveMethod
     *            the method that returns the wrapped primitive
     */
    public FlyFrameworkTypeWrapper(String fullyQualifiedName, String toPrimitiveMethod)
    {
        super(fullyQualifiedName);
        this.toPrimitiveMethod = toPrimitiveMethod;
    }

    public String getToPrimitiveMethod()
    {
        return toPrimitiveMethod;
    }

    public static FlyFrameworkTypeWrapper getFrameworkPagesInstance()
    {
        if (frameworkPagesInstance == null)
        {
            frameworkPagesInstance = new FlyFrameworkTypeWrapper("org.generator.base.page.Pages", //$NON-NLS-1$
                    ""); //$NON-NLS-1$
        }

        return frameworkPagesInstance;
    }

    public static FlyFrameworkTypeWrapper getSpringAnnotationPropagation()
    {
        if (springAnnotationPropagation == null)
        {
            springAnnotationPropagation = new FlyFrameworkTypeWrapper(
                    "org.springframework.transaction.annotation.Propagation", //$NON-NLS-1$
                    ""); //$NON-NLS-1$
        }

        return springAnnotationPropagation;
    }

    public static FlyFrameworkTypeWrapper getFrameworkBaseSericeInstance(String rootInterface,IntrospectedTable introspectedTable)
    {

        if (introspectedTable.getPrimaryKeyColumns().size()==1){
            String baseSericePackage="org.mybatis.generator.base.service.single.{0}<{1},{2}>";
            frameworkBaseSericeInstance = new FlyFrameworkTypeWrapper(MessageFormat.format(baseSericePackage, rootInterface,introspectedTable.getBaseRecordType(),introspectedTable.getExampleType()),"");
        }else{
            String baseSericePackage="org.mybatis.generator.base.service.single.{0}<{1},{2},{3}>";
            frameworkBaseSericeInstance = new FlyFrameworkTypeWrapper(MessageFormat.format(baseSericePackage, rootInterface,introspectedTable.getFullyQualifiedTable(), introspectedTable.getExampleType(), introspectedTable.getGeneratedKey()),"");
        }
        return frameworkBaseSericeInstance;
    }

    public static FlyFrameworkTypeWrapper getFrameworkBaseMapperInstance(String rootInterface,IntrospectedTable introspectedTable)
    {

        if (introspectedTable.getPrimaryKeyColumns().size()==1){
            String baseMapperPackage="org.mybatis.generator.base.mapper.single.{0}<{1},{2}>";
            frameworkBaseMappleInstance = new FlyFrameworkTypeWrapper(MessageFormat.format(baseMapperPackage, rootInterface,introspectedTable.getBaseRecordType(),introspectedTable.getExampleType()),"");
        }else{
            String baseMapperPackage="org.mybatis.generator.base.mapper.single.{0}<{1},{2},{3}>";
            frameworkBaseSericeInstance = new FlyFrameworkTypeWrapper(MessageFormat.format(baseMapperPackage, rootInterface,introspectedTable.getFullyQualifiedTable(), introspectedTable.getExampleType(), introspectedTable.getGeneratedKey()),"");
        }
        return frameworkBaseMappleInstance;
    }
    
    public static FlyFrameworkTypeWrapper getFrameworkBaseControlInstance(String rootInterface)
    {

        //String baseSericePackage="com.cnnct.rabbit.framework.base.{0}";
        frameworkBaseControlInstance = new FlyFrameworkTypeWrapper(rootInterface,"");
        return frameworkBaseControlInstance;
    }
    
    public static FullyQualifiedJavaType getBasicLongInstance()
    {
        if (basicLongInstance == null)
        {
            basicLongInstance = new FlyFrameworkTypeWrapper("long", ""); //$NON-NLS-1$
        }

        return basicLongInstance;
    }

    public static FlyFrameworkTypeWrapper getSpringAnnotationTransactionalInstance()
    {
        if (springAnnotationTransactionalInstance == null)
        {
            springAnnotationTransactionalInstance = new FlyFrameworkTypeWrapper(
                    "org.springframework.transaction.annotation.Transactional", //$NON-NLS-1$
                    ""); //$NON-NLS-1$
        }

        return springAnnotationTransactionalInstance;
    }

    // import com.cnnct.rabbit.framework.base.service.IBaseSngPkService;
}
