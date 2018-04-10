/*
 * Copyright 2008 The Apache Software Foundation
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
package org.mybatis.generator.codegen.mybatis3.dao.serviceimpl;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FlyFrameworkTypeWrapper;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.service.AbstractJavaServiceGenerator;
import org.mybatis.generator.codegen.mybatis3.serviceimp.elements.ConstructorMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.JavaBeansUtil;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class DaoServiceImpGenerator extends AbstractJavaServiceGenerator
{

    /**
     * 
     */
    public DaoServiceImpGenerator()
    {
        super(true);
    }

    public DaoServiceImpGenerator(boolean requiresMatchedXMLGenerator)
    {
        super(requiresMatchedXMLGenerator);
    }

    @Override
	public List<CompilationUnit> getCompilationUnits()
    {
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getMyBatis3DaoServiceImpType());
        TopLevelClass topLevelClass = new TopLevelClass(type);
        // service注解
        topLevelClass.addAnnotation("@org.springframework.stereotype.Service");
        // 导入包
        topLevelClass.addImportedType("javax.annotation.Resource");
        //topLevelClass.addImportedType("org.apache.logging.log4j.LogManager");
        //topLevelClass.addImportedType("org.apache.logging.log4j.Logger");
        
        //topLevelClass.addImportedType("org.apache.log4j.Logger;");
        topLevelClass.addImportedType("org.apache.logging.log4j.LogManager");
        topLevelClass.addImportedType("org.apache.logging.log4j.Logger");
        
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        // 实现接口
        FullyQualifiedJavaType serviceInterfaceType =new FullyQualifiedJavaType(introspectedTable.getMyBatis3DaoServiceType());
        topLevelClass.addSuperInterface(serviceInterfaceType);
        topLevelClass.addImportedType(serviceInterfaceType);
        
        // 继承basesservice类
        String rootInterface = introspectedTable.getTableConfigurationProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        if (!stringHasValue(rootInterface))
        {
            rootInterface = context.getDaoServiceImpGeneratorConfiguration().getProperty(
                    PropertyRegistry.ANY_ROOT_INTERFACE);
        }
        if (stringHasValue(rootInterface))
        {
            FullyQualifiedJavaType parentServiceType = FlyFrameworkTypeWrapper.getFrameworkBaseSericeInstance(rootInterface,
                    introspectedTable);
            topLevelClass.setSuperClass(parentServiceType);
            topLevelClass.addImportedType(parentServiceType);
        }

        progressCallback.startTask(getString("Progress.17", //$NON-NLS-1$
                introspectedTable.getFullyQualifiedTable().toString()));
        // 生成注释
        CommentGenerator commentGenerator = context.getCommentGenerator();
        commentGenerator.addJavaFileComment(topLevelClass, introspectedTable);
        // log4j对象
        Field field = new Field();
        field.setFinal(true);
        field.setStatic(true);
        field.setInitializationString(MessageFormat.format("LogManager.getLogger({0}.class);", type.getShortName()));
        field.setName("log"); //$NON-NLS-1$
        field.setType(new FullyQualifiedJavaType("Logger")); //$NON-NLS-1$
        context.getCommentGenerator().addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);

        // 注解dao对象
        FullyQualifiedJavaType fieldType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        field = new Field(JavaBeansUtil.getValidPropertyName(fieldType.getShortName()), new FullyQualifiedJavaType(
                fieldType.getShortName()));
        topLevelClass.addField(field);

        // 构造方法
        addConstructorMethod(topLevelClass);
        // 分页查询
        // addBrowserImpMethod(topLevelClass);
        // 添加方法
        //addAppendImpMethod(topLevelClass);
        // 删除对象
        //addDeleteImpMethod(topLevelClass);
        // 修改方法
        //addUpdateImpMethod(topLevelClass);
        // 查询唯一对象
        //addSelectUniqueImpMethod(topLevelClass);

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        answer.add(topLevelClass);
        return answer;

    }

    private void addConstructorMethod(TopLevelClass topLevelClass)
    {
        AbstractJavaServiceGenerator methodGenerator = new ConstructorMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }

    

   /* private void addDeleteImpMethod(TopLevelClass topLevelClass)
    {
        AbstractJavaServiceGenerator methodGenerator = new DeleteImpMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }*/


    protected void initializeAndExecuteGenerator(AbstractJavaServiceGenerator methodGenerator,
            TopLevelClass topLevelClass)
    {
        methodGenerator.setContext(context);
        methodGenerator.setIntrospectedTable(introspectedTable);
        methodGenerator.setProgressCallback(progressCallback);
        methodGenerator.setWarnings(warnings);
        methodGenerator.addMethod(topLevelClass);
    }

    public List<CompilationUnit> getExtraCompilationUnits()
    {
        return null;
    }

    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator()
    {
        return new XMLMapperGenerator();
    }

    @Override
    public void addMethod(TopLevelClass topLevelClass)
    {
        
    }

}
