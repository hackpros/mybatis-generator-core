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
public class DbxImpSelectByExampleMethodGenerator extends AbstractJavaServiceGenerator {

	public final static String THIS_METHOD_NAME = "dbxSelectByExample";

	public DbxImpSelectByExampleMethodGenerator() {
		super(true);
	}

	@Override
	public void addMethod(TopLevelClass topLevelClass) {
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
		FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("SoaResult<Pages<" + resParameterType.getShortName() +">>");
		method.setReturnType(returnType);
		method.setName(THIS_METHOD_NAME);
		method.setVisibility(JavaVisibility.PUBLIC);
		context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
		String reqParameterName = JavaBeansUtil.getValidPropertyName(reqParameterType.getShortName());
		method.addParameter(new Parameter(reqParameterType, reqParameterName)); //$NON-NLS-1$
		
		FullyQualifiedJavaType pageParam=new FullyQualifiedJavaType("Pages<" + resParameterType.getShortName() + ">");
	    method.addParameter(new Parameter(pageParam, "page")); //$NON-NLS-1$
	     
	        
	        
		addMapperAnnotations(topLevelClass, method);
		FullyQualifiedJavaType examp = new FullyQualifiedJavaType(introspectedTable.getExampleType());
		
		// 注解dao对象
		FullyQualifiedJavaType fieldType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3DaoServiceType());

		method.addBodyLine(MessageFormat.format("SoaResult<Pages<{0}>> res=new SoaResult<Pages<{0}>>();",resParameterType.getShortName(),resParameterType.getShortName()));
		method.addBodyLine("try {");
		method.addBodyLine(MessageFormat.format("{0} e = new {1}();" ,examp.getShortName(),examp.getShortName()));
		method.addBodyLine(MessageFormat.format("List<{0}> list={1}.selectByExample(e);",introspectedTable.getRules().calculateAllFieldsClass().getShortName(),JavaBeansUtil.firstLetterLower(fieldType.getShortName())));
		method.addBodyLine("res.setCode(0);");
		method.addBodyLine("//res.setData(list);");
		method.addBodyLine("res.setData(page);");
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
