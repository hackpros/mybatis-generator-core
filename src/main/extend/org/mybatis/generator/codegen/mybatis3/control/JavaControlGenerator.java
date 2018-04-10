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
package org.mybatis.generator.codegen.mybatis3.control;

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
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.control.elements.AppendCtlMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.control.elements.BrowserCtlMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.control.elements.DoAppendCtlMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.control.elements.DoBrowserCtlMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.control.elements.RemoveCtlMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.control.elements.DoModifyCtlMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.control.elements.ModifyCtlMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.control.elements.SelectUniqueCtlMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.JavaBeansUtil;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class JavaControlGenerator extends AbstractJavaControlGenerator
{
    /**
     * 
     */
    public JavaControlGenerator()
    {
        super(true);
    }

    public JavaControlGenerator(boolean requiresMatchedXMLGenerator)
    {
        super(requiresMatchedXMLGenerator);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits()
    {
        progressCallback.startTask(getString("Progress.17", //$NON-NLS-1$
                introspectedTable.getFullyQualifiedTable().toString()));

        // 类
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getMyBatis3ControlType());
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.addImportedType(type);
        // bean
        FullyQualifiedJavaType beanType = introspectedTable.getRules().calculateAllFieldsClass();
        Parameter bean = new Parameter(beanType, JavaBeansUtil.getValidPropertyName(beanType.getShortName()));
        topLevelClass.addImportedType(beanType);
        // service注解
        topLevelClass.addAnnotation("@Controller");
        topLevelClass.addAnnotation(MessageFormat.format("@RequestMapping(value=\"/{0}\")", bean.getName()));

        // 导入包
        topLevelClass.addImportedType("javax.annotation.Resource");
        topLevelClass.addImportedType("org.apache.logging.log4j.LogManager");
        topLevelClass.addImportedType("org.apache.logging.log4j.Logger");
        //topLevelClass.addImportedType("org.apache.log4j.Logger");
        topLevelClass.addImportedType("com.jumore.b2b.activity.comm.Pages");
        
        topLevelClass.addImportedType("org.springframework.stereotype.Controller");
        topLevelClass.addImportedType("org.springframework.web.bind.annotation.RequestMapping");
        topLevelClass.addImportedType("org.springframework.ui.Model");
        topLevelClass.addImportedType("java.util.List");
        topLevelClass.addImportedType("org.springframework.web.bind.annotation.RequestMethod");
        topLevelClass.addImportedType("org.springframework.web.bind.annotation.RequestParam");
        topLevelClass.addImportedType("org.springframework.web.bind.annotation.RequestBody");

        
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);

        // 实现接口 contrl 实现接口
        // topLevelClass.addSuperInterface(new FullyQualifiedJavaType(introspectedTable.getMyBatis3ServiceType()));

        // 继承baseControl类
        String rootInterface = introspectedTable.getTableConfigurationProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        if (!stringHasValue(rootInterface))
        {
            rootInterface = context.getJavaControlGeneratorConfiguration().getProperty(
                    PropertyRegistry.ANY_ROOT_INTERFACE);
        }
        if (stringHasValue(rootInterface))
        {
            FullyQualifiedJavaType fqjt = FlyFrameworkTypeWrapper.getFrameworkBaseControlInstance(rootInterface);
            topLevelClass.setSuperClass(fqjt);
            topLevelClass.addImportedType(fqjt);
        }

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

        // 注入service
        FullyQualifiedJavaType fieldType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3DbxServiceType());
        field = new Field(JavaBeansUtil.firstLetterLower(fieldType.getShortName()), new FullyQualifiedJavaType(
                fieldType.getShortName()));
        field.addAnnotation("@Resource");
        topLevelClass.addField(field);
        topLevelClass.addImportedType(fieldType);

        // 分页查询
        addBrowserCtlMethod(topLevelClass);
        addDoBrowserCtlMethod(topLevelClass);
        // 添加方法
        addAppendCtlMethod(topLevelClass);
        addDoAppendCtlMethod(topLevelClass);
        // 删除对象
        addRemoveCtlMethod(topLevelClass);
        // 修改方法
        addModifyCtlMethod(topLevelClass);
        addDoModifyCtlMethod(topLevelClass);
        // 查询对象详情
        addSelectUniqueCtlMethod(topLevelClass);

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        answer.add(topLevelClass);
        return answer;
    }

    protected void addBrowserCtlMethod(TopLevelClass topLevelClass)
    {
        AbstractJavaControlGenerator methodGenerator = new BrowserCtlMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }

    protected void addDoBrowserCtlMethod(TopLevelClass topLevelClass)
    {
        AbstractJavaControlGenerator methodGenerator = new DoBrowserCtlMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }

    protected void addAppendCtlMethod(TopLevelClass topLevelClass)
    {
        AbstractJavaControlGenerator methodGenerator = new AppendCtlMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }

    protected void addDoAppendCtlMethod(TopLevelClass topLevelClass)
    {
        AbstractJavaControlGenerator methodGenerator = new DoAppendCtlMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }

    protected void addSelectUniqueCtlMethod(TopLevelClass topLevelClass)
    {
        AbstractJavaControlGenerator methodGenerator = new SelectUniqueCtlMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }

    protected void addRemoveCtlMethod(TopLevelClass topLevelClass)
    {
        AbstractJavaControlGenerator methodGenerator = new RemoveCtlMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }

    protected void addModifyCtlMethod(TopLevelClass topLevelClass)
    {
        AbstractJavaControlGenerator methodGenerator = new ModifyCtlMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }

    protected void addDoModifyCtlMethod(TopLevelClass topLevelClass)
    {
        AbstractJavaControlGenerator methodGenerator = new DoModifyCtlMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, topLevelClass);
    }

    protected void initializeAndExecuteGenerator(AbstractJavaControlGenerator methodGenerator,
            TopLevelClass topLevelClass)
    {
        methodGenerator.setContext(context);
        methodGenerator.setIntrospectedTable(introspectedTable);
        methodGenerator.setProgressCallback(progressCallback);
        methodGenerator.setWarnings(warnings);
        methodGenerator.init();
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

    @Override
    public void init()
    {
        // TODO Auto-generated method stub

    }

}
