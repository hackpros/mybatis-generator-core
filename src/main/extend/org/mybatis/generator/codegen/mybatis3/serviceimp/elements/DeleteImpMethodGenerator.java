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

import java.util.List;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FlyFrameworkTypeWrapper;
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
public class DeleteImpMethodGenerator extends
        AbstractJavaServiceGenerator {

    public final static String THIS_METHOD_NAME="delete";
    
    
    public DeleteImpMethodGenerator() {
        super(true);
    }
    @Override
    public void addMethod(TopLevelClass topLevelClass) {
    	//FullyQualifiedJavaType mapperType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
		//String mapperFild = JavaBeansUtil.getValidPropertyName(mapperType.getShortName());

		Method method = new Method();

		method.setReturnType(FlyFrameworkTypeWrapper.getBasicLongInstance());
		method.setName(THIS_METHOD_NAME);
		method.setVisibility(JavaVisibility.PUBLIC);

		FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(
                "java.lang.List<"+introspectedTable.getRules().calculateAllFieldsClass().getShortName()+">"); //$NON-NLS-1$

		Parameter parameter = new Parameter(parameterType,
				JavaBeansUtil.getValidPropertyName(introspectedTable.getRules().calculateAllFieldsClass().getShortName()));
		
		
		method.addParameter(parameter); // $NON-NLS-1$

		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

		addMapperAnnotations(topLevelClass, method);

		method.addBodyLine("/** 删除务逻辑 **/");
		method.addBodyLine("");
		method.addBodyLine("log.info(\"devlopping\");");
		method.addBodyLine("");
		method.addBodyLine("/** 删除业务逻辑完 **/");
		method.addBodyLine("/**######################_我是分隔线######################**/");
		method.addBodyLine("");
		method.addBodyLine("return 0;");

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
