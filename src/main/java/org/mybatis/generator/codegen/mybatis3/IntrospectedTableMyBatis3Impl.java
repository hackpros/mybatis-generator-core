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
package org.mybatis.generator.codegen.mybatis3;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedUIFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.HtmlDocument;
import org.mybatis.generator.codegen.AbstractGenerator;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.control.AbstractJavaControlGenerator;
import org.mybatis.generator.codegen.mybatis3.control.JavaControlGenerator;
import org.mybatis.generator.codegen.mybatis3.dao.service.DaoServiceGenerator;
import org.mybatis.generator.codegen.mybatis3.dao.serviceimpl.DaoServiceImpGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.AnnotatedClientGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.JavaMapperGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.MixedClientGenerator;
import org.mybatis.generator.codegen.mybatis3.model.BaseRecordGenerator;
import org.mybatis.generator.codegen.mybatis3.model.BusinessReqModelGenerator;
import org.mybatis.generator.codegen.mybatis3.model.BusinessResModelGenerator;
import org.mybatis.generator.codegen.mybatis3.model.ExampleGenerator;
import org.mybatis.generator.codegen.mybatis3.model.PrimaryKeyGenerator;
import org.mybatis.generator.codegen.mybatis3.model.RecordWithBLOBsGenerator;
import org.mybatis.generator.codegen.mybatis3.service.AbstractJavaServiceGenerator;
import org.mybatis.generator.codegen.mybatis3.service.JavaServiceGenerator;
import org.mybatis.generator.codegen.mybatis3.serviceimp.JavaServiceImpGenerator;
import org.mybatis.generator.codegen.mybatis3.soa.dbx.DbxApiGenerator;
import org.mybatis.generator.codegen.mybatis3.soa.dbx.DbxServiceApiGenerator;
import org.mybatis.generator.codegen.mybatis3.ui.AbstractUIGenerator;
import org.mybatis.generator.codegen.mybatis3.ui.easyui.EasyUIGenerator;
import org.mybatis.generator.codegen.mybatis3.ui.jdl.JDLUIGenerator;
import org.mybatis.generator.codegen.mybatis3.ui.macui.MacUIGenerator;
import org.mybatis.generator.codegen.mybatis3.ui.metronicbootstrapui.MetronicBootstrapUIGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.UIGeneratorConfiguration;
import org.mybatis.generator.internal.ObjectFactory;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class IntrospectedTableMyBatis3Impl extends IntrospectedTable {
	protected List<AbstractJavaGenerator> javaModelGenerators;

	protected List<AbstractJavaGenerator> clientGenerators;

	protected List<AbstractUIGenerator> uiGenerators;

	protected AbstractXmlGenerator xmlMapperGenerator;

	public IntrospectedTableMyBatis3Impl() {
		super(TargetRuntime.MYBATIS3);
		javaModelGenerators = new ArrayList<AbstractJavaGenerator>();
		clientGenerators = new ArrayList<AbstractJavaGenerator>();
		uiGenerators = new ArrayList<AbstractUIGenerator>();
	}

	@Override
	public void calculateGenerators(List<String> warnings, ProgressCallback progressCallback) {
		calculateJavaModelGenerators(warnings, progressCallback);

		calculateUIGenerators(warnings, progressCallback);

		AbstractJavaClientGenerator javaClientGenerator = calculateClientGenerators(warnings, progressCallback);

		calculateXmlMapperGenerator(javaClientGenerator, warnings, progressCallback);
	}

	protected void calculateXmlMapperGenerator(AbstractJavaClientGenerator javaClientGenerator, List<String> warnings,
			ProgressCallback progressCallback) {
		if (javaClientGenerator == null) {
			if (context.getSqlMapGeneratorConfiguration() != null) {
				xmlMapperGenerator = new XMLMapperGenerator();
			}
		} else {
			xmlMapperGenerator = javaClientGenerator.getMatchedXMLGenerator();
		}

		initializeAbstractGenerator(xmlMapperGenerator, warnings, progressCallback);
	}

	/**
	 * 
	 * @param warnings
	 * @param progressCallback
	 * @return true if an XML generator is required
	 */
	protected AbstractJavaClientGenerator calculateClientGenerators(List<String> warnings, ProgressCallback progressCallback) {
		if (!rules.generateJavaClient()) {
			return null;
		}

		AbstractJavaClientGenerator javaGenerator = createJavaClientGenerator();
		if (javaGenerator == null) {
			return null;
		}

		initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
		clientGenerators.add(javaGenerator);

		// #############################
		// service生成器
		JavaServiceGenerator javaServiceGenerator = createJavaServiceGenerator();
		if (javaServiceGenerator != null) {
			// return null;
			initializeAbstractGenerator(javaServiceGenerator, warnings, progressCallback);
			clientGenerators.add(javaServiceGenerator);
		}
		JavaServiceImpGenerator javaServiceImpGenerator = createJavaServiceImpGenerator();
		if (javaServiceImpGenerator != null) {
			initializeAbstractGenerator(javaServiceImpGenerator, warnings, progressCallback);
			clientGenerators.add(javaServiceImpGenerator);
		}

		// #############################
		// control实现类生成器
		AbstractJavaControlGenerator javaControlGenerator = createJavaControlGenerator();
		if (javaControlGenerator != null) {
			initializeAbstractGenerator(javaControlGenerator, warnings, progressCallback);
			clientGenerators.add(javaControlGenerator);
		}
		// #############################

		// 第四层dao接口生成实现类生成器
		AbstractJavaServiceGenerator daoServiceGenerator = createDaoServiceGenerator();
		if (daoServiceGenerator != null) {
			initializeAbstractGenerator(daoServiceGenerator, warnings, progressCallback);
			clientGenerators.add(daoServiceGenerator);
		}
		AbstractJavaServiceGenerator daoImpServiceGenerator = createDaoServiceImpGenerator();
		if (daoImpServiceGenerator != null) {
			initializeAbstractGenerator(daoImpServiceGenerator, warnings, progressCallback);
			clientGenerators.add(daoImpServiceGenerator);
		}
		// #############################

		// dubbox分布式服务生成
		AbstractJavaServiceGenerator dbxServiceGenerator = createDbxServiceGenerator();
		if (dbxServiceGenerator != null) {
			initializeAbstractGenerator(dbxServiceGenerator, warnings, progressCallback);
			clientGenerators.add(dbxServiceGenerator);
		}
		AbstractJavaServiceGenerator dbxServiceImpGenerator = createDbxServiceImpGenerator();
		if (dbxServiceImpGenerator != null) {
			initializeAbstractGenerator(dbxServiceImpGenerator, warnings, progressCallback);
			clientGenerators.add(dbxServiceImpGenerator);
		}
		// #############################

		return javaGenerator;

	}

	// service接口实现类生成器
	protected JavaServiceGenerator createJavaServiceGenerator() {
		if (context.getJavaServiceGeneratorConfiguration() == null) {
			return null;
		}
		return new JavaServiceGenerator();
	}

	// #############################
	// service实现类生成器
	protected JavaServiceImpGenerator createJavaServiceImpGenerator() {
		if (context.getJavaServiceImpGeneratorConfiguration() == null)

		{
			return null;
		}
		return new JavaServiceImpGenerator();
	}

	// #############################
	// control接口生成
	protected AbstractJavaControlGenerator createJavaControlGenerator() {
		if (context.getJavaControlGeneratorConfiguration() == null)
		// if (context.getJavaServiceImpGeneratorConfiguration() == null)
		{
			return null;
		}
		return new JavaControlGenerator();
	}

	// #############################

	// daoService接口生成
	protected AbstractJavaServiceGenerator createDaoServiceGenerator() {
		if (context.getDaoServiceGeneratorConfiguration() == null) {
			return null;
		}
		return new DaoServiceGenerator();
	}

	// #############################

	// daoService接口生成
	protected AbstractJavaServiceGenerator createDaoServiceImpGenerator() {
		if (context.getDaoServiceImpGeneratorConfiguration() == null) {
			return null;
		}
		return new DaoServiceImpGenerator();
	}

	// #############################

	// service接口实现类生成器
	protected DbxApiGenerator createDbxServiceGenerator() {
		if (context.getDbxServiceGeneratorConfiguration() == null) {
			return null;
		}
		return new DbxApiGenerator();
	}

	// #############################
	// service实现类生成器
	protected DbxServiceApiGenerator createDbxServiceImpGenerator() {
		if (context.getDbxServiceImpGeneratorConfiguration() == null)

		{
			return null;
		}
		return new DbxServiceApiGenerator();
	}

	// #############################

	// #############################
	// 业务模型对象与单元测试
	/*
	 * protected DbxServiceApiGenerator createDbxServiceImpGenerator() { if
	 * (context.getDbxServiceImpGeneratorConfiguration() == null)
	 * 
	 * { return null; } return new DbxServiceApiGenerator(); }
	 * 
	 * protected DbxServiceApiGenerator createDbxServiceImpGenerator() { if
	 * (context.getDbxServiceImpGeneratorConfiguration() == null)
	 * 
	 * { return null; } return new DbxServiceApiGenerator(); }
	 */

	// #############################

	// uid页面生成
	protected AbstractUIGenerator createUIGenerator() {
		if (context.getUiGeneratorConfiguration() == null) {
			return null;
		}

		String type = context.getUiGeneratorConfiguration().getConfigurationType();

		AbstractUIGenerator uiGenerator;
		if ("EASYUI".equalsIgnoreCase(type)) { //$NON-NLS-1$
			uiGenerator = new EasyUIGenerator();
		} else if ("METRONICBOOTSTRAPUI".equalsIgnoreCase(type)) { //$NON-NLS-1$
			uiGenerator = new MetronicBootstrapUIGenerator();
		} else if ("MACUI".equalsIgnoreCase(type)) { //$NON-NLS-1$
			uiGenerator = new MacUIGenerator();
		} else if ("JDL".equalsIgnoreCase(type)) { //$NON-NLS-1$
			uiGenerator = new JDLUIGenerator();
		} else {
			uiGenerator = (EasyUIGenerator) ObjectFactory.createInternalObject(type);
		}
		return uiGenerator;

	}

	protected AbstractJavaClientGenerator createJavaClientGenerator() {
		if (context.getJavaClientGeneratorConfiguration() == null) {
			return null;
		}

		String type = context.getJavaClientGeneratorConfiguration().getConfigurationType();

		AbstractJavaClientGenerator javaGenerator;
		if ("XMLMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
			javaGenerator = new JavaMapperGenerator();
		} else if ("MIXEDMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
			javaGenerator = new MixedClientGenerator();
		} else if ("ANNOTATEDMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
			javaGenerator = new AnnotatedClientGenerator();
		} else if ("MAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
			javaGenerator = new JavaMapperGenerator();
		} else {
			javaGenerator = (AbstractJavaClientGenerator) ObjectFactory.createInternalObject(type);
		}

		return javaGenerator;
	}

	protected void calculateJavaModelGenerators(List<String> warnings, ProgressCallback progressCallback) {
		if (getRules().generateExampleClass()) {
			AbstractJavaGenerator javaGenerator = new ExampleGenerator();
			initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
			javaModelGenerators.add(javaGenerator);
		}

		if (getRules().generatePrimaryKeyClass()) {
			AbstractJavaGenerator javaGenerator = new PrimaryKeyGenerator();
			initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
			javaModelGenerators.add(javaGenerator);
		}

		if (getRules().generateBaseRecordClass()) {
			AbstractJavaGenerator javaGenerator = new BaseRecordGenerator();
			initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
			javaModelGenerators.add(javaGenerator);

			AbstractJavaGenerator reqModelGenerator = new BusinessReqModelGenerator();
			initializeAbstractGenerator(reqModelGenerator, warnings, progressCallback);
			javaModelGenerators.add(reqModelGenerator);

			AbstractJavaGenerator resModelGenerator = new BusinessResModelGenerator();
			initializeAbstractGenerator(resModelGenerator, warnings, progressCallback);
			javaModelGenerators.add(resModelGenerator);

		}

		if (getRules().generateRecordWithBLOBsClass()) {
			AbstractJavaGenerator javaGenerator = new RecordWithBLOBsGenerator();
			initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
			javaModelGenerators.add(javaGenerator);
		}
	}

	protected void calculateUIGenerators(List<String> warnings, ProgressCallback progressCallback) {
		// #############################
		// ui实现类生成器
		AbstractUIGenerator uilGenerator = createUIGenerator();
		initializeAbstractGenerator(uilGenerator, warnings, progressCallback);
		uiGenerators.add(uilGenerator);
	}

	protected void initializeAbstractGenerator(AbstractGenerator abstractGenerator, List<String> warnings, ProgressCallback progressCallback) {
		if (abstractGenerator == null) {
			return;
		}

		abstractGenerator.setContext(context);
		abstractGenerator.setIntrospectedTable(this);
		abstractGenerator.setProgressCallback(progressCallback);
		abstractGenerator.setWarnings(warnings);
	}

	@Override
	public List<GeneratedJavaFile> getGeneratedJavaFiles() {
		List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();

		for (AbstractJavaGenerator javaGenerator : javaModelGenerators) {

			List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
			for (CompilationUnit compilationUnit : compilationUnits) {

				String targetProject = context.getJavaModelGeneratorConfiguration().getTargetProject();
				if (javaGenerator instanceof BusinessReqModelGenerator || javaGenerator instanceof BusinessResModelGenerator) {
					targetProject = context.getJavaBusinessModelGeneratorConfiguration().getTargetProject();
				}
				GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit, targetProject,
						context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING), context.getJavaFormatter());
				answer.add(gjf);
			}
		}

		for (AbstractJavaGenerator javaGenerator : clientGenerators) {
			List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
			for (CompilationUnit compilationUnit : compilationUnits) {
				String targetProject = context.getJavaClientGeneratorConfiguration().getTargetProject();

				if (javaGenerator instanceof JavaControlGenerator) {
					targetProject = context.getJavaControlGeneratorConfiguration().getTargetProject();

				} else if (javaGenerator instanceof DaoServiceGenerator) {
					targetProject = context.getDaoServiceGeneratorConfiguration().getTargetProject();
				} else if (javaGenerator instanceof DaoServiceGenerator) {
					targetProject = context.getDaoServiceGeneratorConfiguration().getTargetProject();
				} else if (javaGenerator instanceof DbxApiGenerator) {
					targetProject = context.getDbxServiceGeneratorConfiguration().getTargetProject();
				} else if (javaGenerator instanceof DbxServiceApiGenerator) {
					targetProject = context.getDbxServiceImpGeneratorConfiguration().getTargetProject();
				} else if (javaGenerator instanceof DbxServiceApiGenerator) {
					targetProject = context.getDbxServiceImpGeneratorConfiguration().getTargetProject();

				}

				GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit, targetProject,
						context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING), context.getJavaFormatter());
				answer.add(gjf);
			}
		}

		return answer;
	}

	@Override
	public List<GeneratedXmlFile> getGeneratedXmlFiles() {
		List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();

		if (xmlMapperGenerator != null) {
			Document document = xmlMapperGenerator.getDocument();
			GeneratedXmlFile gxf = new GeneratedXmlFile(document, getMyBatis3XmlMapperFileName(), getMyBatis3XmlMapperPackage(), context
					.getSqlMapGeneratorConfiguration().getTargetProject(), false, context.getXmlFormatter());
			if (context.getPlugins().sqlMapGenerated(gxf, this)) {
				answer.add(gxf);
			}
		}

		return answer;
	}

	@Override
	public int getGenerationSteps() {
		return javaModelGenerators.size() + clientGenerators.size() + (xmlMapperGenerator == null ? 0 : 1);
	}

	@Override
	public boolean isJava5Targeted() {
		return true;
	}

	@Override
	public boolean requiresXMLGenerator() {
		AbstractJavaClientGenerator javaClientGenerator = createJavaClientGenerator();

		if (javaClientGenerator == null) {
			return false;
		} else {
			return javaClientGenerator.requiresXMLGenerator();
		}
	}

	@Override
	public List<GeneratedUIFile> getGeneratedUIFiles(IntrospectedTable introspectedTable) {
		List<GeneratedUIFile> answer = new ArrayList<GeneratedUIFile>();
		UIGeneratorConfiguration uiConfig = context.getUiGeneratorConfiguration();

		if (uiConfig == null) {
			return answer;
		}

		
		
		for (AbstractUIGenerator uiGenerator : uiGenerators) {
			
			//如果是jdl 就走这里
			if (uiGenerator instanceof JDLUIGenerator){
				HtmlDocument document = uiGenerator.getJDLFileDocument();
				
				GeneratedUIFile gxf = new GeneratedUIFile(document, introspectedTable.getFullyQualifiedTable().toString()+".jh", getMyBatis3UIPackage(), uiConfig.getTargetProject(),
						false, context.getUIFormatter());
				answer.add(gxf);
				continue;
			}
			String enableTreePro = uiConfig.getProperty(AbstractUIGenerator.ENABLE_TREE);
			String enableBrowser = uiConfig.getProperty(AbstractUIGenerator.ENABLE_BROWSER);
			String enableAppend = uiConfig.getProperty(AbstractUIGenerator.ENABLE_APPEND);
			String enableModify = uiConfig.getProperty(AbstractUIGenerator.ENABLE_MODIFY);
			String enableQueryFormPro = uiConfig.getProperty(AbstractUIGenerator.ENABLE_QUERYFORM);
			String javaScriptPackage = uiConfig.getProperty(AbstractUIGenerator.JAVASCRIPT_PACKAGE);
			/**
			 * 是否生成树弄目录
			 */
			boolean enableTree = enableTreePro != null && enableTreePro.equalsIgnoreCase("true");
			boolean enableQueryForm = enableQueryFormPro != null && enableQueryFormPro.equalsIgnoreCase("true");
			/**
			 * 生成browser页
			 */
			if (enableBrowser != null && enableBrowser.equalsIgnoreCase("true")) {
				HtmlDocument document = uiGenerator.getBrowserHtmlDocument(enableTree, enableQueryForm);
				GeneratedUIFile gxf = new GeneratedUIFile(document, "borrow.vm", getMyBatis3UIPackage(), javaScriptPackage,
						false, context.getUIFormatter());
				answer.add(gxf);
			}
			/**
			 * 生成添加页
			 */
			if (enableAppend != null && enableAppend.equalsIgnoreCase("true")) {
				HtmlDocument document = uiGenerator.getAppendHtmlDocument();
				GeneratedUIFile gxf = new GeneratedUIFile(document, "append.vm", getMyBatis3UIPackage(), uiConfig.getTargetProject(),
						false, context.getUIFormatter());
				answer.add(gxf);
			}
			/**
			 * 生成修改页
			 */
			if (enableModify != null && enableModify.equalsIgnoreCase("true")) {
				HtmlDocument document = uiGenerator.getModifyHtmlDocument();
				GeneratedUIFile gxf = new GeneratedUIFile(document, "modify.vm", getMyBatis3UIPackage(), uiConfig.getTargetProject(),
						false, context.getUIFormatter());
				answer.add(gxf);
			}
			/**
			 * 生成查询页
			 */

			if (enableQueryForm) {
				HtmlDocument document = uiGenerator.getQueryFormHtmlDocument();
				GeneratedUIFile gxf = new GeneratedUIFile(document, "query.vm", getMyBatis3UIPackage(), uiConfig.getTargetProject(), false,
						context.getUIFormatter());
				answer.add(gxf);
			}
			/**
			 * 生成查询页js
			 */
			if (javaScriptPackage != null && !javaScriptPackage.equals("")) {
				HtmlDocument document = uiGenerator.getDocument();
				GeneratedUIFile gxf = new GeneratedUIFile(document, "browser.js", getMyBatis3UIPackage(), javaScriptPackage, false,
						context.getUIFormatter());
				answer.add(gxf);
			}
		}

		return answer;
	}
}
