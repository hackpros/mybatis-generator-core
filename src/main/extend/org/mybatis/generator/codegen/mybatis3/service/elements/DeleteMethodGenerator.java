/*
 *  Copyright 2009 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.service.elements;

import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.FlyFrameworkTypeWrapper;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.internal.util.JavaBeansUtil;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class DeleteMethodGenerator extends
        AbstractJavaMapperMethodGenerator {

    public final static String THIS_METHOD_NAME="delete";
    
    public DeleteMethodGenerator() {
        super();
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        

        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        importedTypes.add(FlyFrameworkTypeWrapper.getFrameworkPagesInstance());
        importedTypes.add(FlyFrameworkTypeWrapper.getSpringAnnotationPropagation());
        importedTypes.add(FlyFrameworkTypeWrapper.getSpringAnnotationTransactionalInstance());
        
        FullyQualifiedJavaType parameterType = introspectedTable.getRules()
                .calculateAllFieldsClass();
        importedTypes.add(parameterType);
        
        Method method = new Method();
        
        method.setReturnType(FlyFrameworkTypeWrapper.getBasicLongInstance());
        method.setName(THIS_METHOD_NAME);
        
        
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(
                "java.lang.List<"+introspectedTable.getTableConfiguration().getDomainObjectName()+">"); //$NON-NLS-1$

        method.setName(THIS_METHOD_NAME);
        method.addParameter(new Parameter(fqjt, JavaBeansUtil.getValidPropertyName(introspectedTable.getRules().calculateAllFieldsClass().getShortName()))); //$NON-NLS-1$

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        addMapperAnnotations(interfaze, method);
        
        if (context.getPlugins().clientCountByExampleMethodGenerated(method,
                interfaze, introspectedTable)) {
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }

    public void addMapperAnnotations(Interface interfaze, Method method) {
        method.addJavaDocLine("/**"); //$NON-NLS-1$
        method.addJavaDocLine(" *删除"); //$NON-NLS-1$
        method.addJavaDocLine(" */"); //$NON-NLS-1$
        method.addAnnotation("@Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)");
        
    }
}
