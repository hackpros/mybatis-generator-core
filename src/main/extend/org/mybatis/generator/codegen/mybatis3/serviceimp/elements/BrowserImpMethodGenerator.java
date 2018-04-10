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
public class BrowserImpMethodGenerator extends AbstractJavaServiceGenerator
{

    public final static String THIS_METHOD_NAME = "browser";

    public BrowserImpMethodGenerator()
    {
        super(true);
    }

    @Override
    public void addMethod(TopLevelClass topLevelClass)
    {

        // 导入分页插件
        topLevelClass.addImportedType("com.github.pagehelper.PageHelper");
        topLevelClass.addImportedType("com.github.pagehelper.PageInfo");

        FullyQualifiedJavaType mapperType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());

        String mapperField = JavaBeansUtil.getValidPropertyName(mapperType.getShortName());
        FullyQualifiedJavaType parameterType = introspectedTable.getRules().calculateAllFieldsClass();

        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedTable.getExampleType());

        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        importedTypes.add(fqjt);
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());

        Method method = new Method();
        FullyQualifiedJavaType returnFqjt = new FullyQualifiedJavaType("Pages<" + parameterType.getShortName() + ">"); //$NON-NLS-1$
        method.setReturnType(returnFqjt);
        method.setName(THIS_METHOD_NAME);
        method.setVisibility(JavaVisibility.PUBLIC);

        importedTypes.add(parameterType);
        method.addParameter(new Parameter(parameterType, JavaBeansUtil.getValidPropertyName(introspectedTable
                .getTableConfiguration().getDomainObjectName()))); //$NON-NLS-1$

        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "length")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "offset")); //$NON-NLS-1$

        method.addBodyLine(MessageFormat.format("{0} example = new {1}();", fqjt.getShortName(), fqjt.getShortName()));
        method.addBodyLine("/** 查询业务逻辑 **/");
        method.addBodyLine("");
        method.addBodyLine("// example.createCriteria().andXXXEqualTo(xx.())");
        method.addBodyLine("");
        method.addBodyLine(" /** 查询业务逻辑完 **/");
        method.addBodyLine("/**######################_我是分隔线######################**/");
        method.addBodyLine("");
        method.addBodyLine(MessageFormat.format("List<{0}> list = new ArrayList<{1}>();", parameterType.getShortName(),
                parameterType.getShortName()));
        method.addBodyLine(MessageFormat.format("long total = {0}.countByExample(example);", mapperField));
        method.addBodyLine("if (total > 0) {");
        method.addBodyLine("/**排序业务逻辑 **/");
        method.addBodyLine("//example.setOrderByClause(XX)");
        method.addBodyLine("/** 排序业务逻辑完 **/");
        method.addBodyLine("/**######################_我是分隔线######################**/");
        method.addBodyLine("//分页插件查询");
        method.addBodyLine("PageHelper.startPage(offset, length);");
        method.addBodyLine(MessageFormat.format("list = {0}.selectByExample(example);", mapperField));
        method.addBodyLine(MessageFormat.format("PageInfo<{0}> page = new PageInfo<{0}>(list);",
                parameterType.getShortName()));
        method.addBodyLine("list=page.getList();");

        method.addBodyLine("}");
        method.addBodyLine(MessageFormat.format("return new Pages<{0}>(list, total, offset, length);",
                parameterType.toString()));

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        addMapperAnnotations(topLevelClass, method);

        if (context.getPlugins().clientCountByExampleMethodGenerated(method, topLevelClass, introspectedTable))
        {
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }

    public void addMapperAnnotations(TopLevelClass topLevelClass, Method method)
    {
        method.addJavaDocLine("/**"); //$NON-NLS-1$
        method.addJavaDocLine("*综合查询"); //$NON-NLS-1$
        method.addJavaDocLine(" */"); //$NON-NLS-1$
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
