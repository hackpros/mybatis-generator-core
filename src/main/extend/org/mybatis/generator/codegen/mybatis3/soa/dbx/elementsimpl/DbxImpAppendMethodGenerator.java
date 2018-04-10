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
package org.mybatis.generator.codegen.mybatis3.soa.dbx.elementsimpl;

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
public class DbxImpAppendMethodGenerator extends AbstractJavaServiceGenerator {

	public final static String THIS_METHOD_NAME = "dbxAppend";

	public DbxImpAppendMethodGenerator() {
		super(true);
	}

	@Override
	public void addMethod(TopLevelClass topLevelClass) {
		topLevelClass.addImportedType("org.apache.commons.lang3.RandomStringUtils");
		Method method = new Method();
		FullyQualifiedJavaType reqParameterType = null;
		FullyQualifiedJavaType resParameterType = null;

		if (context.getJavaBusinessModelGeneratorConfiguration() == null) {
			reqParameterType = introspectedTable.getRules().calculateAllFieldsClass();
			resParameterType = reqParameterType;
		} else {
			reqParameterType = introspectedTable.getReqFullyQualifiedJavaType();
			resParameterType = introspectedTable.getResFullyQualifiedJavaType();
		}
		FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("SoaResult<" + resParameterType.getShortName() + ">");
		method.setReturnType(returnType);
		method.setName(THIS_METHOD_NAME);


		method.setVisibility(JavaVisibility.PUBLIC);
		
		method.addParameter(new Parameter(reqParameterType, reqParameterType.getShortName())); //$NON-NLS-1$

		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		addMapperAnnotations(topLevelClass, method);

		
		String mapperFild = JavaBeansUtil.getValidPropertyName(resParameterType.getShortName());
        
		method.addBodyLine(MessageFormat.format("SoaResult<{0}> res=new SoaResult<{1}>();",resParameterType.getShortName(),resParameterType.getShortName()));
		method.addBodyLine("try {");
		method.addBodyLine(MessageFormat.format("{0} t = new {1}();",introspectedTable.getRules().calculateAllFieldsClass().getShortName(),
				introspectedTable.getRules().calculateAllFieldsClass().getShortName()));
		introspectedTable.getRules().calculateAllFieldsClass();
		method.addBodyLine("//################### check param ");
		method.addBodyLine("");
		method.addBodyLine("appCustomerService.insert(t);");
		method.addBodyLine("res.setCode(0);");
		method.addBodyLine(MessageFormat.format("{0} {1} = new {2}();",resParameterType.getShortName(),mapperFild,resParameterType.getShortName()));
		method.addBodyLine(MessageFormat.format("SpringBeanUtils.copyProperties(t, {0});",mapperFild));
		method.addBodyLine(MessageFormat.format("res.setData({0});",mapperFild));
		method.addBodyLine("} catch (Exception e) {");
		method.addBodyLine("String bizCode=RandomStringUtils.randomNumeric(16);");
		method.addBodyLine("log.error(e.getMessage()+\"code:\",bizCode);");
		method.addBodyLine("res.setCode(-1);");
		method.addBodyLine("res.setMsg(\"业务异常,code={\"+bizCode+\"}\");");
		method.addBodyLine("}");
		method.addBodyLine("return res;");

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
	public AbstractXmlGenerator getMatchedXMLGenerator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CompilationUnit> getCompilationUnits() {
		// TODO Auto-generated method stub
		return null;
	}
}
