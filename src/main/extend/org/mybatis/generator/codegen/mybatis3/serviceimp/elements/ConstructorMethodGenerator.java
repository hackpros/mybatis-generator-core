/*
 * Copyright 2009 The Apache Software Foundation
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
package org.mybatis.generator.codegen.mybatis3.serviceimp.elements;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.service.AbstractJavaServiceGenerator;
import org.mybatis.generator.internal.util.JavaBeansUtil;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class ConstructorMethodGenerator extends AbstractJavaServiceGenerator
{

    public final static String THIS_METHOD_NAME = "append";

    public ConstructorMethodGenerator()
    {
        super(true);
    }

    @Override
    public void addMethod(TopLevelClass topLevelClass)
    {

        // 导入对象
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();

        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        topLevelClass.addImportedType(introspectedTable.getMyBatis3JavaMapperType());
        // 方法
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);

        method.setName(JavaBeansUtil.getSetterMethodName(parameterType.getShortName()));
        method.addAnnotation("@Resource");

        // 方法参数
        importedTypes.add(parameterType);
        Parameter parameter = new Parameter(parameterType, JavaBeansUtil.getValidPropertyName(parameterType
                .getShortName()));

        method.addParameter(parameter);
        StringBuilder sb = new StringBuilder();
        sb.append(MessageFormat.format("this.{0}={0};", parameter.getName()));
        method.addBodyLine(sb.toString());
        sb.setLength(0);
        sb.append(MessageFormat.format("this.setBaseMapper({0});",
                JavaBeansUtil.getValidPropertyName(parameterType.getShortName())));
        method.addBodyLine(sb.toString());

        addMapperAnnotations(topLevelClass, method);
        if (context.getPlugins().clientCountByExampleMethodGenerated(method, topLevelClass, introspectedTable))
        {
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }

    public void addMapperAnnotations(TopLevelClass topLevelClass, Method method)
    {
    }

    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<CompilationUnit> getCompilationUnits()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
