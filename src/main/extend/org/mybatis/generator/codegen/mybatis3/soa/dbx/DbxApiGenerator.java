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
package org.mybatis.generator.codegen.mybatis3.soa.dbx;

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
import org.mybatis.generator.codegen.mybatis3.soa.dbx.elements.DbxAppendMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.soa.dbx.elements.DbxCountByExampleMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.soa.dbx.elements.DbxDeleteByExampleMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.soa.dbx.elements.DbxDeleteByPrimaryKeyMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.soa.dbx.elements.DbxModifyMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.soa.dbx.elements.DbxSelectByExampleMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.soa.dbx.elements.DbxSelectByPrimaryKeyMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.soa.dbx.elements.DbxSelectPageMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.config.PropertyRegistry;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class DbxApiGenerator extends AbstractJavaServiceGenerator {

	/**
     * 
     */
	public DbxApiGenerator() {
		super(true);
	}

	public DbxApiGenerator(boolean requiresMatchedXMLGenerator) {
		super(requiresMatchedXMLGenerator);
	}

	@Override
	public List<CompilationUnit> getCompilationUnits() {
		progressCallback.startTask(getString("Progress.17", //$NON-NLS-1$
				introspectedTable.getFullyQualifiedTable().toString()));
		CommentGenerator commentGenerator = context.getCommentGenerator();

		FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getMyBatis3DbxServiceType());
		Interface interfaze = new Interface(type);

		FullyQualifiedJavaType pagesType = new FullyQualifiedJavaType("com.jumore.b2b.activity.comm.Pages");
		interfaze.addImportedType(pagesType);

		FullyQualifiedJavaType soaResultType = new FullyQualifiedJavaType("com.jumore.b2b.activity.comm.SoaResult");
		interfaze.addImportedType(soaResultType);
		
		if (context.getJavaBusinessModelGeneratorConfiguration()==null){
			FullyQualifiedJavaType basetype = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
			interfaze.addImportedType(basetype);
		}else{
			interfaze.addImportedType(introspectedTable.getReqFullyQualifiedJavaType());
			interfaze.addImportedType(introspectedTable.getResFullyQualifiedJavaType());
		}

		interfaze.setVisibility(JavaVisibility.PUBLIC);
		commentGenerator.addJavaFileComment(interfaze, introspectedTable);

		String rootInterface = introspectedTable.getTableConfigurationProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
		if (!stringHasValue(rootInterface)) {
			rootInterface = context.getDbxServiceGeneratorConfiguration().getProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
		}

		if (stringHasValue(rootInterface)) {
			FullyQualifiedJavaType fqjt = FlyFrameworkTypeWrapper.getFrameworkBaseSericeInstance(rootInterface, introspectedTable);
			interfaze.addSuperInterface(fqjt);
			// interfaze.addImportedType(fqjt);
		}

		addDbxAppendMethod(interfaze);
		addDbxCountByExampleMethod(interfaze);
		addDbxDeleteByExampleMethod(interfaze);
		addDbxDeleteByPrimaryKeyExampleMethod(interfaze);
		addDbxModifyMethod(interfaze);
		addDbxSelectByExampleMethod(interfaze);
		addDbxSelectByPrimaryKeyMethod(interfaze);
		addDbxSelectPageMethod(interfaze);

		List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
		if (context.getPlugins().clientGenerated(interfaze, null, introspectedTable)) {
			answer.add(interfaze);
		}

		List<CompilationUnit> extraCompilationUnits = getExtraCompilationUnits();
		if (extraCompilationUnits != null) {
			answer.addAll(extraCompilationUnits);
		}

		return answer;
	}

	protected void addDbxAppendMethod(Interface interfaze) {
		AbstractJavaMapperMethodGenerator methodGenerator = new DbxAppendMethodGenerator();
		initializeAndExecuteGenerator(methodGenerator, interfaze);
	}

	protected void addDbxCountByExampleMethod(Interface interfaze) {
		AbstractJavaMapperMethodGenerator methodGenerator = new DbxCountByExampleMethodGenerator();
		initializeAndExecuteGenerator(methodGenerator, interfaze);
	}

	protected void addDbxDeleteByExampleMethod(Interface interfaze) {
		AbstractJavaMapperMethodGenerator methodGenerator = new DbxDeleteByExampleMethodGenerator();
		initializeAndExecuteGenerator(methodGenerator, interfaze);
	}

	protected void addDbxDeleteByPrimaryKeyExampleMethod(Interface interfaze) {
		AbstractJavaMapperMethodGenerator methodGenerator = new DbxDeleteByPrimaryKeyMethodGenerator();
		initializeAndExecuteGenerator(methodGenerator, interfaze);
	}

	protected void addDbxModifyMethod(Interface interfaze) {
		AbstractJavaMapperMethodGenerator methodGenerator = new DbxModifyMethodGenerator();
		initializeAndExecuteGenerator(methodGenerator, interfaze);
	}

	protected void addDbxSelectByExampleMethod(Interface interfaze) {
		AbstractJavaMapperMethodGenerator methodGenerator = new DbxSelectByExampleMethodGenerator();
		initializeAndExecuteGenerator(methodGenerator, interfaze);
	}

	protected void addDbxSelectByPrimaryKeyMethod(Interface interfaze) {
		AbstractJavaMapperMethodGenerator methodGenerator = new DbxSelectByPrimaryKeyMethodGenerator();
		initializeAndExecuteGenerator(methodGenerator, interfaze);
	}

	protected void addDbxSelectPageMethod(Interface interfaze) {
		AbstractJavaMapperMethodGenerator methodGenerator = new DbxSelectPageMethodGenerator();
		initializeAndExecuteGenerator(methodGenerator, interfaze);
	}

	protected void initializeAndExecuteGenerator(AbstractJavaMapperMethodGenerator methodGenerator, Interface interfaze) {
		methodGenerator.setContext(context);
		methodGenerator.setIntrospectedTable(introspectedTable);
		methodGenerator.setProgressCallback(progressCallback);
		methodGenerator.setWarnings(warnings);
		methodGenerator.addInterfaceElements(interfaze);
	}

	public List<CompilationUnit> getExtraCompilationUnits() {
		return null;
	}

	@Override
	public AbstractXmlGenerator getMatchedXMLGenerator() {
		return new XMLMapperGenerator();
	}

	@Override
	public void addMethod(TopLevelClass topLevelClass) {

	}

}
