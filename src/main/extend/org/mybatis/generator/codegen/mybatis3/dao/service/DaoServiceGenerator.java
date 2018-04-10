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
package org.mybatis.generator.codegen.mybatis3.dao.service;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FlyFrameworkTypeWrapper;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.service.AbstractJavaServiceGenerator;
import org.mybatis.generator.codegen.mybatis3.service.elements.AppendMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.service.elements.BrowserMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.service.elements.DeleteMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.service.elements.SelectUniqueMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.service.elements.UpdateMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.config.PropertyRegistry;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class DaoServiceGenerator extends AbstractJavaServiceGenerator
{

    /**
     * 
     */
    public DaoServiceGenerator()
    {
        super(true);
    }

    public DaoServiceGenerator(boolean requiresMatchedXMLGenerator)
    {
        super(requiresMatchedXMLGenerator);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits()
    {
        progressCallback.startTask(getString("Progress.17", //$NON-NLS-1$
                introspectedTable.getFullyQualifiedTable().toString()));
        CommentGenerator commentGenerator = context.getCommentGenerator();

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getMyBatis3DaoServiceType());
        Interface interfaze = new Interface(type);
        
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(interfaze, introspectedTable);

        String rootInterface = introspectedTable.getTableConfigurationProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        if (!stringHasValue(rootInterface))
        {
            rootInterface = context.getDaoServiceGeneratorConfiguration().getProperty(
                    PropertyRegistry.ANY_ROOT_INTERFACE);
        }

        if (stringHasValue(rootInterface))
        {

            FullyQualifiedJavaType fqjt = FlyFrameworkTypeWrapper.getFrameworkBaseSericeInstance(rootInterface,introspectedTable);
            interfaze.addSuperInterface(fqjt);
            interfaze.addImportedType(fqjt);
        }
        
        //addBrowserMethod(interfaze);
       // addAppendMethod(interfaze);
       // addDeleteMethod(interfaze);
       // addUpdateMethod(interfaze);
       // addSelectUniqueMethod(interfaze);
        
        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (context.getPlugins().clientGenerated(interfaze, null, introspectedTable))
        {
            answer.add(interfaze);
        }

        List<CompilationUnit> extraCompilationUnits = getExtraCompilationUnits();
        if (extraCompilationUnits != null)
        {
            answer.addAll(extraCompilationUnits);
        }

        return answer;
    }

    protected void addBrowserMethod(Interface interfaze)
    {
        AbstractJavaMapperMethodGenerator methodGenerator = new BrowserMethodGenerator();
        initializeAndExecuteGenerator(methodGenerator, interfaze);
    }

    protected void addAppendMethod(Interface interfaze)
    {
            AbstractJavaMapperMethodGenerator methodGenerator = new AppendMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
    }

    protected void addDeleteMethod(Interface interfaze)
    {
            AbstractJavaMapperMethodGenerator methodGenerator = new DeleteMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
    }
    protected void addUpdateMethod(Interface interfaze)
    {
            AbstractJavaMapperMethodGenerator methodGenerator = new UpdateMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
    }
    protected void addSelectUniqueMethod(Interface interfaze)
    {
            AbstractJavaMapperMethodGenerator methodGenerator = new SelectUniqueMethodGenerator();
            initializeAndExecuteGenerator(methodGenerator, interfaze);
    }

    protected void initializeAndExecuteGenerator(AbstractJavaMapperMethodGenerator methodGenerator, Interface interfaze)
    {
        methodGenerator.setContext(context);
        methodGenerator.setIntrospectedTable(introspectedTable);
        methodGenerator.setProgressCallback(progressCallback);
        methodGenerator.setWarnings(warnings);
        methodGenerator.addInterfaceElements(interfaze);
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
