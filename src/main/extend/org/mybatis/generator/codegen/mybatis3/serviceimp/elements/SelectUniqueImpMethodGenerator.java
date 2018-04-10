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
package org.mybatis.generator.codegen.mybatis3.serviceimp.elements;

import java.text.MessageFormat;
import java.util.List;

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
public class SelectUniqueImpMethodGenerator extends
        AbstractJavaServiceGenerator {

    public final static String THIS_METHOD_NAME="selectUnique";
    
    
    public SelectUniqueImpMethodGenerator() {
        super(true);
    }
    @Override
    public void addMethod(TopLevelClass topLevelClass) {
        

    	FullyQualifiedJavaType mapperType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
		String mapperFild = JavaBeansUtil.getValidPropertyName(mapperType.getShortName());
		FullyQualifiedJavaType parameterType = introspectedTable.getRules().calculateAllFieldsClass();
		
		Method method = new Method();

		method.setReturnType(parameterType);
		method.setName(THIS_METHOD_NAME);
		method.setVisibility(JavaVisibility.PUBLIC);

		
		Parameter parameter = new Parameter(parameterType,
				JavaBeansUtil.getValidPropertyName(parameterType.getShortName()));
		method.addParameter(parameter); // $NON-NLS-1$

		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		addMapperAnnotations(topLevelClass, method);
		
		FullyQualifiedJavaType examp = new FullyQualifiedJavaType(
                introspectedTable.getExampleType());
		
		method.addBodyLine(MessageFormat.format("{0} e = new {0}();",examp.getShortName()));
		method.addBodyLine("/**　查询业务逻辑 **/");
		method.addBodyLine("");
		method.addBodyLine("");
		method.addBodyLine(" /** 查询业务逻辑完 **/");
		method.addBodyLine("/**######################_我是分隔线######################**/");
		method.addBodyLine(MessageFormat.format("List<{0}> list = {1}.selectByExample(e);",parameterType.getShortName(),mapperFild));
		method.addBodyLine("if (list.size() != 1) ");
		method.addBodyLine("throw new RuntimeException(\"对象不存在!\"); ");
		method.addBodyLine("return list.get(0);");

		if (context.getPlugins().clientCountByExampleMethodGenerated(method, topLevelClass, introspectedTable)) {
			topLevelClass.addMethod(method);
		}
		
		

    }

    public void addMapperAnnotations(TopLevelClass topLevelClass, Method method) {
        method.addJavaDocLine("/**"); //$NON-NLS-1$
        method.addJavaDocLine(" *添加"); //$NON-NLS-1$
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
