/*
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
package org.mybatis.generator.config.xml;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.mybatis.generator.config.ColumnOverride;
import org.mybatis.generator.config.ColumnRenamingRule;
import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.DaoServiceGeneratorConfiguration;
import org.mybatis.generator.config.DaoServiceImpGeneratorConfiguration;
import org.mybatis.generator.config.DbxServiceGeneratorConfiguration;
import org.mybatis.generator.config.DbxServiceImpGeneratorConfiguration;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.config.IgnoredColumn;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.JavaBusinessModelGeneratorConfiguration;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaControlGeneratorConfiguration;
import org.mybatis.generator.config.JavaJunitGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.JavaServiceGeneratorConfiguration;
import org.mybatis.generator.config.JavaServiceImpGeneratorConfiguration;
import org.mybatis.generator.config.JavaTypeResolverConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.PropertyHolder;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.config.UIGeneratorConfiguration;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.util.StringUtility;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class parses configuration files into the new Configuration API
 * 
 * @author Jeff Butler
 */
public class MyBatisGeneratorConfigurationParser
{
    private Properties properties;

    public MyBatisGeneratorConfigurationParser(Properties properties)
    {
        super();
        if (properties == null)
        {
            this.properties = System.getProperties();
        }
        else
        {
            this.properties = properties;
        }
    }

    public Configuration parseConfiguration(Element rootNode) throws XMLParserException
    {

        Configuration configuration = new Configuration();

        NodeList nodeList = rootNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            if ("properties".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperties(configuration, childNode);
            }
            else if ("classPathEntry".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseClassPathEntry(configuration, childNode);
            }
            else if ("context".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseContext(configuration, childNode);
            }
        }

        return configuration;
    }

    private void parseProperties(Configuration configuration, Node node) throws XMLParserException
    {
        Properties attributes = parseAttributes(node);
        String resource = attributes.getProperty("resource"); //$NON-NLS-1$
        String url = attributes.getProperty("url"); //$NON-NLS-1$

        if (!stringHasValue(resource) && !stringHasValue(url))
        {
            throw new XMLParserException(getString("RuntimeError.14")); //$NON-NLS-1$
        }

        if (stringHasValue(resource) && stringHasValue(url))
        {
            throw new XMLParserException(getString("RuntimeError.14")); //$NON-NLS-1$
        }

        URL resourceUrl;

        try
        {
            if (stringHasValue(resource))
            {
                resourceUrl = ObjectFactory.getResource(resource);
                if (resourceUrl == null)
                {
                    throw new XMLParserException(getString("RuntimeError.15", resource)); //$NON-NLS-1$
                }
            }
            else
            {
                resourceUrl = new URL(url);
            }

            InputStream inputStream = resourceUrl.openConnection().getInputStream();

            properties.load(inputStream);
            inputStream.close();
        }
        catch(IOException e)
        {
            if (stringHasValue(resource))
            {
                throw new XMLParserException(getString("RuntimeError.16", resource)); //$NON-NLS-1$
            }
            else
            {
                throw new XMLParserException(getString("RuntimeError.17", url)); //$NON-NLS-1$
            }
        }
    }

    private void parseContext(Configuration configuration, Node node)
    {

        Properties attributes = parseAttributes(node);
        String defaultModelType = attributes.getProperty("defaultModelType"); //$NON-NLS-1$
        String targetRuntime = attributes.getProperty("targetRuntime"); //$NON-NLS-1$
        String introspectedColumnImpl = attributes.getProperty("introspectedColumnImpl"); //$NON-NLS-1$
        String id = attributes.getProperty("id"); //$NON-NLS-1$

        ModelType mt = defaultModelType == null ? null : ModelType.getModelType(defaultModelType);

        Context context = new Context(mt);
        context.setId(id);
        if (stringHasValue(introspectedColumnImpl))
        {
            context.setIntrospectedColumnImpl(introspectedColumnImpl);
        }
        if (stringHasValue(targetRuntime))
        {
            context.setTargetRuntime(targetRuntime);
        }

        configuration.addContext(context);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }
            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(context, childNode);
            }
            else if ("plugin".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parsePlugin(context, childNode);
            }
            else if ("commentGenerator".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseCommentGenerator(context, childNode);
            }
            else if ("jdbcConnection".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseJdbcConnection(context, childNode);
            }
            else if ("javaModelGenerator".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseJavaModelGenerator(context, childNode);
            }
            else if ("javaTypeResolver".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseJavaTypeResolver(context, childNode);
            }
            else if ("sqlMapGenerator".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseSqlMapGenerator(context, childNode);
            }
            else if ("javaClientGenerator".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseJavaClientGenerator(context, childNode);
            }
            else if ("javaServiceGenerator".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseJavaServiceGenerator(context, childNode);
            }
            else if ("javaServiceImpGenerator".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseJavaServiceImpGenerator(context, childNode);
            }
            else if ("daoServiceGenerator".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseDaoServiceGenerator(context, childNode);
            }
            else if ("daoServiceImpGenerator".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseDaoServiceImpGenerator(context, childNode);
            }
            else if ("dbxServiceGenerator".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseDbxServiceGenerator(context, childNode);
            }
            else if ("dbxServiceImpGenerator".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseDbxServiceImpGenerator(context, childNode);
            }
            
            
            else if ("javaBusinessModelGenerator".equals(childNode.getNodeName())) { //$NON-NLS-1$
            	 parseJavaaBusinessModelGenerator(context, childNode);
            }
            else if ("javaJunitGenerator".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseJavaJunitGenerator(context, childNode);
            }
            
            
            
            else if ("javaControlGenerator".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseJavaControlGenerator(context, childNode);
            }
            else if ("uiGenerator".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseUIGenerator(context, childNode);
            }
            else if ("table".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseTable(context, childNode);
            }
        }
    }

    private void parseSqlMapGenerator(Context context, Node node)
    {
        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = new SqlMapGeneratorConfiguration();

        context.setSqlMapGeneratorConfiguration(sqlMapGeneratorConfiguration);

        Properties attributes = parseAttributes(node);
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$

        sqlMapGeneratorConfiguration.setTargetPackage(targetPackage);
        sqlMapGeneratorConfiguration.setTargetProject(targetProject);

        // ############################################
        // ####### 2015-08-06　author renntrabbit@foxmail.com
        /**
         * 是否生成正文初始化
         */
        boolean targetBody = StringUtility
                .isTrue(attributes.getProperty("targetBody") == null ? "true" : attributes.getProperty("targetBody")); //$NON-NLS-1$
        sqlMapGeneratorConfiguration.setTargetBody(targetBody);
        // ############################################

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(sqlMapGeneratorConfiguration, childNode);
            }
        }

        context.getSqlMapGeneratorConfigurations().add(sqlMapGeneratorConfiguration);
    }

    private void parseTable(Context context, Node node)
    {
        TableConfiguration tc = new TableConfiguration(context);
        context.addTableConfiguration(tc);

        Properties attributes = parseAttributes(node);
        String catalog = attributes.getProperty("catalog"); //$NON-NLS-1$
        String schema = attributes.getProperty("schema"); //$NON-NLS-1$
        String tableName = attributes.getProperty("tableName"); //$NON-NLS-1$
        String domainObjectName = attributes.getProperty("domainObjectName"); //$NON-NLS-1$
        String alias = attributes.getProperty("alias"); //$NON-NLS-1$
        String enableInsert = attributes.getProperty("enableInsert"); //$NON-NLS-1$
        String enableSelectByPrimaryKey = attributes.getProperty("enableSelectByPrimaryKey"); //$NON-NLS-1$
        String enableSelectByExample = attributes.getProperty("enableSelectByExample"); //$NON-NLS-1$
        String enableUpdateByPrimaryKey = attributes.getProperty("enableUpdateByPrimaryKey"); //$NON-NLS-1$
        String enableDeleteByPrimaryKey = attributes.getProperty("enableDeleteByPrimaryKey"); //$NON-NLS-1$
        String enableDeleteByExample = attributes.getProperty("enableDeleteByExample"); //$NON-NLS-1$
        String enableCountByExample = attributes.getProperty("enableCountByExample"); //$NON-NLS-1$
        String enableUpdateByExample = attributes.getProperty("enableUpdateByExample"); //$NON-NLS-1$
        String selectByPrimaryKeyQueryId = attributes.getProperty("selectByPrimaryKeyQueryId"); //$NON-NLS-1$
        String selectByExampleQueryId = attributes.getProperty("selectByExampleQueryId"); //$NON-NLS-1$
        String modelType = attributes.getProperty("modelType"); //$NON-NLS-1$
        String escapeWildcards = attributes.getProperty("escapeWildcards"); //$NON-NLS-1$
        String delimitIdentifiers = attributes.getProperty("delimitIdentifiers"); //$NON-NLS-1$
        String delimitAllColumns = attributes.getProperty("delimitAllColumns"); //$NON-NLS-1$

        if (stringHasValue(catalog))
        {
            tc.setCatalog(catalog);
        }

        if (stringHasValue(schema))
        {
            tc.setSchema(schema);
        }

        if (stringHasValue(tableName))
        {
            tc.setTableName(tableName);
        }

        if (stringHasValue(domainObjectName))
        {
            tc.setDomainObjectName(domainObjectName);
        }

        if (stringHasValue(alias))
        {
            tc.setAlias(alias);
        }

        if (stringHasValue(enableInsert))
        {
            tc.setInsertStatementEnabled(isTrue(enableInsert));
        }

        if (stringHasValue(enableSelectByPrimaryKey))
        {
            tc.setSelectByPrimaryKeyStatementEnabled(isTrue(enableSelectByPrimaryKey));
        }

        if (stringHasValue(enableSelectByExample))
        {
            tc.setSelectByExampleStatementEnabled(isTrue(enableSelectByExample));
        }

        if (stringHasValue(enableUpdateByPrimaryKey))
        {
            tc.setUpdateByPrimaryKeyStatementEnabled(isTrue(enableUpdateByPrimaryKey));
        }

        if (stringHasValue(enableDeleteByPrimaryKey))
        {
            tc.setDeleteByPrimaryKeyStatementEnabled(isTrue(enableDeleteByPrimaryKey));
        }

        if (stringHasValue(enableDeleteByExample))
        {
            tc.setDeleteByExampleStatementEnabled(isTrue(enableDeleteByExample));
        }

        if (stringHasValue(enableCountByExample))
        {
            tc.setCountByExampleStatementEnabled(isTrue(enableCountByExample));
        }

        if (stringHasValue(enableUpdateByExample))
        {
            tc.setUpdateByExampleStatementEnabled(isTrue(enableUpdateByExample));
        }

        if (stringHasValue(selectByPrimaryKeyQueryId))
        {
            tc.setSelectByPrimaryKeyQueryId(selectByPrimaryKeyQueryId);
        }

        if (stringHasValue(selectByExampleQueryId))
        {
            tc.setSelectByExampleQueryId(selectByExampleQueryId);
        }

        if (stringHasValue(modelType))
        {
            tc.setConfiguredModelType(modelType);
        }

        if (stringHasValue(escapeWildcards))
        {
            tc.setWildcardEscapingEnabled(isTrue(escapeWildcards));
        }

        if (stringHasValue(delimitIdentifiers))
        {
            tc.setDelimitIdentifiers(isTrue(delimitIdentifiers));
        }

        if (stringHasValue(delimitAllColumns))
        {
            tc.setAllColumnDelimitingEnabled(isTrue(delimitAllColumns));
        }

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(tc, childNode);
            }
            else if ("columnOverride".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseColumnOverride(tc, childNode);
            }
            else if ("ignoreColumn".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseIgnoreColumn(tc, childNode);
            }
            else if ("generatedKey".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseGeneratedKey(tc, childNode);
            }
            else if ("columnRenamingRule".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseColumnRenamingRule(tc, childNode);
            }
        }
    }

    private void parseColumnOverride(TableConfiguration tc, Node node)
    {
        Properties attributes = parseAttributes(node);
        String column = attributes.getProperty("column"); //$NON-NLS-1$
        String property = attributes.getProperty("property"); //$NON-NLS-1$
        String javaType = attributes.getProperty("javaType"); //$NON-NLS-1$
        String jdbcType = attributes.getProperty("jdbcType"); //$NON-NLS-1$
        String typeHandler = attributes.getProperty("typeHandler"); //$NON-NLS-1$
        String delimitedColumnName = attributes.getProperty("delimitedColumnName"); //$NON-NLS-1$

        ColumnOverride co = new ColumnOverride(column);

        if (stringHasValue(property))
        {
            co.setJavaProperty(property);
        }

        if (stringHasValue(javaType))
        {
            co.setJavaType(javaType);
        }

        if (stringHasValue(jdbcType))
        {
            co.setJdbcType(jdbcType);
        }

        if (stringHasValue(typeHandler))
        {
            co.setTypeHandler(typeHandler);
        }

        if (stringHasValue(delimitedColumnName))
        {
            co.setColumnNameDelimited(isTrue(delimitedColumnName));
        }

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(co, childNode);
            }
        }

        tc.addColumnOverride(co);
    }

    private void parseGeneratedKey(TableConfiguration tc, Node node)
    {
        Properties attributes = parseAttributes(node);

        String column = attributes.getProperty("column"); //$NON-NLS-1$
        boolean identity = isTrue(attributes.getProperty("identity")); //$NON-NLS-1$
        String sqlStatement = attributes.getProperty("sqlStatement"); //$NON-NLS-1$
        String type = attributes.getProperty("type"); //$NON-NLS-1$

        GeneratedKey gk = new GeneratedKey(column, sqlStatement, identity, type);

        tc.setGeneratedKey(gk);
    }

    private void parseIgnoreColumn(TableConfiguration tc, Node node)
    {
        Properties attributes = parseAttributes(node);
        String column = attributes.getProperty("column"); //$NON-NLS-1$
        String delimitedColumnName = attributes.getProperty("delimitedColumnName"); //$NON-NLS-1$

        IgnoredColumn ic = new IgnoredColumn(column);

        if (stringHasValue(delimitedColumnName))
        {
            ic.setColumnNameDelimited(isTrue(delimitedColumnName));
        }

        tc.addIgnoredColumn(ic);
    }

    private void parseColumnRenamingRule(TableConfiguration tc, Node node)
    {
        Properties attributes = parseAttributes(node);
        String searchString = attributes.getProperty("searchString"); //$NON-NLS-1$
        String replaceString = attributes.getProperty("replaceString"); //$NON-NLS-1$

        ColumnRenamingRule crr = new ColumnRenamingRule();

        crr.setSearchString(searchString);

        if (stringHasValue(replaceString))
        {
            crr.setReplaceString(replaceString);
        }

        tc.setColumnRenamingRule(crr);
    }

    private void parseJavaTypeResolver(Context context, Node node)
    {
        JavaTypeResolverConfiguration javaTypeResolverConfiguration = new JavaTypeResolverConfiguration();

        context.setJavaTypeResolverConfiguration(javaTypeResolverConfiguration);

        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$

        if (stringHasValue(type))
        {
            javaTypeResolverConfiguration.setConfigurationType(type);
        }

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(javaTypeResolverConfiguration, childNode);
            }
        }
    }

    private void parsePlugin(Context context, Node node)
    {
        PluginConfiguration pluginConfiguration = new PluginConfiguration();

        context.addPluginConfiguration(pluginConfiguration);

        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$

        pluginConfiguration.setConfigurationType(type);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(pluginConfiguration, childNode);
            }
        }
    }

    private void parseJavaModelGenerator(Context context, Node node)
    {
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = new JavaModelGeneratorConfiguration();

        context.setJavaModelGeneratorConfiguration(javaModelGeneratorConfiguration);

        Properties attributes = parseAttributes(node);
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$

        javaModelGeneratorConfiguration.setTargetPackage(targetPackage);
        javaModelGeneratorConfiguration.setTargetProject(targetProject);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(javaModelGeneratorConfiguration, childNode);
            }
        }
    }

    private void parseJavaClientGenerator(Context context, Node node)
    {
        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = new JavaClientGeneratorConfiguration();

        context.setJavaClientGeneratorConfiguration(javaClientGeneratorConfiguration);

        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$
        String implementationPackage = attributes.getProperty("implementationPackage"); //$NON-NLS-1$

        javaClientGeneratorConfiguration.setConfigurationType(type);
        javaClientGeneratorConfiguration.setTargetPackage(targetPackage);
        javaClientGeneratorConfiguration.setTargetProject(targetProject);
        javaClientGeneratorConfiguration.setImplementationPackage(implementationPackage);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(javaClientGeneratorConfiguration, childNode);
            }
        }
    }
    //####################2016-10-19
    
    /**
     * 
     * 
     * @param context
     * @param node
     *            设定文件
     * @throws
     * @see [类、类#方法、类#成员]
     * @since p2p_cloud_v1.0
     */
    private void parseDaoServiceGenerator(Context context, Node node)

    {
        DaoServiceGeneratorConfiguration daoServiceGeneratorConfiguration = new DaoServiceGeneratorConfiguration();
        context.setDaoServiceGeneratorConfiguration(daoServiceGeneratorConfiguration);
        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$
        String implementationPackage = attributes.getProperty("implementationPackage"); //$NON-NLS-1$

        daoServiceGeneratorConfiguration.setConfigurationType(type);
        daoServiceGeneratorConfiguration.setTargetPackage(targetPackage);
        daoServiceGeneratorConfiguration.setTargetProject(targetProject);
        daoServiceGeneratorConfiguration.setImplementationPackage(implementationPackage);
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }
            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(daoServiceGeneratorConfiguration, childNode);
            }
        }
    }

    /**
     * 
     * service 实现类生成器
     * 
     * @param context
     * @param node
     *            设定文件
     * @throws
     * @see [类、类#方法、类#成员]
     * @since p2p_cloud_v1.0
     */
	private void parseDaoServiceImpGenerator(Context context, Node node)

    {
        DaoServiceImpGeneratorConfiguration generatorCfg = new DaoServiceImpGeneratorConfiguration();

        context.setDaoServiceImpGeneratorConfiguration(generatorCfg);

        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$
        String implementationPackage = attributes.getProperty("implementationPackage"); //$NON-NLS-1$

        generatorCfg.setConfigurationType(type);
        generatorCfg.setTargetPackage(targetPackage);
        generatorCfg.setTargetProject(targetProject);
        generatorCfg.setImplementationPackage(implementationPackage);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(generatorCfg, childNode);
            }
        }
    }

    /**
     * 
     * 
     * @param context
     * @param node
     *            设定文件
     * @throws
     * @see [类、类#方法、类#成员]
     * @since p2p_cloud_v1.0
     */
    private void parseDbxServiceGenerator(Context context, Node node)

    {
    	DbxServiceGeneratorConfiguration dbxServiceGeneratorConfiguration = new DbxServiceGeneratorConfiguration();
        context.setDbxServiceGeneratorConfiguration(dbxServiceGeneratorConfiguration);
        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$
        String implementationPackage = attributes.getProperty("implementationPackage"); //$NON-NLS-1$

        dbxServiceGeneratorConfiguration.setConfigurationType(type);
        dbxServiceGeneratorConfiguration.setTargetPackage(targetPackage);
        dbxServiceGeneratorConfiguration.setTargetProject(targetProject);
        dbxServiceGeneratorConfiguration.setImplementationPackage(implementationPackage);
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }
            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(dbxServiceGeneratorConfiguration, childNode);
            }
        }
    }

    /**
     * 
     * dbx service 实现类生成器
     * 
     * @param context
     * @param node
     *            设定文件
     * @throws
     * @see [类、类#方法、类#成员]
     * @since p2p_cloud_v1.0
     */
    private void parseDbxServiceImpGenerator(Context context, Node node)

    {
    	DbxServiceImpGeneratorConfiguration generatorCfg = new DbxServiceImpGeneratorConfiguration();

        context.setDbxServiceImpGeneratorConfiguration(generatorCfg);

        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$
        String implementationPackage = attributes.getProperty("implementationPackage"); //$NON-NLS-1$

        generatorCfg.setConfigurationType(type);
        generatorCfg.setTargetPackage(targetPackage);
        generatorCfg.setTargetProject(targetProject);
        generatorCfg.setImplementationPackage(implementationPackage);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(generatorCfg, childNode);
            }
        }
    }
    /**
     * 
     * @param context
     * @param node
     */
    private void parseJavaaBusinessModelGenerator(Context context, Node node)

    {
    	JavaBusinessModelGeneratorConfiguration generatorCfg = new JavaBusinessModelGeneratorConfiguration();

        context.setJavaBusinessModelGeneratorConfiguration(generatorCfg);

        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$
        String implementationPackage = attributes.getProperty("implementationPackage"); //$NON-NLS-1$

        generatorCfg.setConfigurationType(type);
        generatorCfg.setTargetPackage(targetPackage);
        generatorCfg.setTargetProject(targetProject);
        generatorCfg.setImplementationPackage(implementationPackage);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(generatorCfg, childNode);
            }
        }
    }
    
    private void parseJavaJunitGenerator(Context context, Node node)

    {
    	JavaJunitGeneratorConfiguration generatorCfg = new JavaJunitGeneratorConfiguration();

        context.setJavaJunitGeneratorConfiguration(generatorCfg);

        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$
        String implementationPackage = attributes.getProperty("implementationPackage"); //$NON-NLS-1$

        generatorCfg.setConfigurationType(type);
        generatorCfg.setTargetPackage(targetPackage);
        generatorCfg.setTargetProject(targetProject);
        generatorCfg.setImplementationPackage(implementationPackage);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(generatorCfg, childNode);
            }
        }
    }
    //####################
    /**
     * 
     * 
     * @param context
     * @param node
     *            设定文件
     * @throws
     * @see [类、类#方法、类#成员]
     * @since p2p_cloud_v1.0
     */
    private void parseJavaServiceGenerator(Context context, Node node)

    {
        JavaServiceGeneratorConfiguration javaServiceGeneratorConfiguration = new JavaServiceGeneratorConfiguration();
        context.setJavaServiceGeneratorConfiguration(javaServiceGeneratorConfiguration);
        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$
        String implementationPackage = attributes.getProperty("implementationPackage"); //$NON-NLS-1$

        javaServiceGeneratorConfiguration.setConfigurationType(type);
        javaServiceGeneratorConfiguration.setTargetPackage(targetPackage);
        javaServiceGeneratorConfiguration.setTargetProject(targetProject);
        javaServiceGeneratorConfiguration.setImplementationPackage(implementationPackage);
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }
            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(javaServiceGeneratorConfiguration, childNode);
            }
        }
    }

    /**
     * 
     * service 实现类生成器
     * 
     * @param context
     * @param node
     *            设定文件
     * @throws
     * @see [类、类#方法、类#成员]
     * @since p2p_cloud_v1.0
     */
    private void parseJavaServiceImpGenerator(Context context, Node node)

    {
        JavaServiceImpGeneratorConfiguration generatorCfg = new JavaServiceImpGeneratorConfiguration();

        context.setJavaServiceImpGeneratorConfiguration(generatorCfg);

        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$
        String implementationPackage = attributes.getProperty("implementationPackage"); //$NON-NLS-1$

        generatorCfg.setConfigurationType(type);
        generatorCfg.setTargetPackage(targetPackage);
        generatorCfg.setTargetProject(targetProject);
        generatorCfg.setImplementationPackage(implementationPackage);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(generatorCfg, childNode);
            }
        }
    }

    /**
     * service 实现类生成器
     * 
     * @param context
     * @param node
     *            设定文件
     */
    private void parseJavaControlGenerator(Context context, Node node)

    {
        JavaControlGeneratorConfiguration generatorCfg = new JavaControlGeneratorConfiguration();

        context.setJavaControlGeneratorConfiguration(generatorCfg);

        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$
        String implementationPackage = attributes.getProperty("implementationPackage"); //$NON-NLS-1$

        generatorCfg.setConfigurationType(type);
        generatorCfg.setTargetPackage(targetPackage);
        generatorCfg.setTargetProject(targetProject);
        generatorCfg.setImplementationPackage(implementationPackage);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(generatorCfg, childNode);
            }
        }
    }

    /**
     * 生成ui页面
     * 
     * @param context
     * @param node
     *            设定文件
     */
    private void parseUIGenerator(Context context, Node node)

    {
        UIGeneratorConfiguration generatorCfg = new UIGeneratorConfiguration();

        context.setUiGeneratorConfiguration(generatorCfg);

        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$
        String targetPackage = attributes.getProperty("targetPackage"); //$NON-NLS-1$
        String targetProject = attributes.getProperty("targetProject"); //$NON-NLS-1$
        String implementationPackage = attributes.getProperty("implementationPackage"); //$NON-NLS-1$

        generatorCfg.setConfigurationType(type);
        generatorCfg.setTargetPackage(targetPackage);
        generatorCfg.setTargetProject(targetProject);
        generatorCfg.setImplementationPackage(implementationPackage);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(generatorCfg, childNode);
            }
        }
    }

    private void parseJdbcConnection(Context context, Node node)
    {
        JDBCConnectionConfiguration jdbcConnectionConfiguration = new JDBCConnectionConfiguration();

        context.setJdbcConnectionConfiguration(jdbcConnectionConfiguration);

        Properties attributes = parseAttributes(node);
        String driverClass = attributes.getProperty("driverClass"); //$NON-NLS-1$
        String connectionURL = attributes.getProperty("connectionURL"); //$NON-NLS-1$
        String userId = attributes.getProperty("userId"); //$NON-NLS-1$
        String password = attributes.getProperty("password"); //$NON-NLS-1$

        jdbcConnectionConfiguration.setDriverClass(driverClass);
        jdbcConnectionConfiguration.setConnectionURL(connectionURL);

        if (stringHasValue(userId))
        {
            jdbcConnectionConfiguration.setUserId(userId);
        }

        if (stringHasValue(password))
        {
            jdbcConnectionConfiguration.setPassword(password);
        }

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(jdbcConnectionConfiguration, childNode);
            }
        }
    }

    private void parseClassPathEntry(Configuration configuration, Node node)
    {
        Properties attributes = parseAttributes(node);

        configuration.addClasspathEntry(attributes.getProperty("location")); //$NON-NLS-1$
    }

    private void parseProperty(PropertyHolder propertyHolder, Node node)
    {
        Properties attributes = parseAttributes(node);

        String name = attributes.getProperty("name"); //$NON-NLS-1$
        String value = attributes.getProperty("value"); //$NON-NLS-1$

        propertyHolder.addProperty(name, value);
    }

    private Properties parseAttributes(Node node)
    {
        Properties attributes = new Properties();
        NamedNodeMap nnm = node.getAttributes();
        for (int i = 0; i < nnm.getLength(); i++)
        {
            Node attribute = nnm.item(i);
            String value = parsePropertyTokens(attribute.getNodeValue());
            attributes.put(attribute.getNodeName(), value);
        }

        return attributes;
    }

    private String parsePropertyTokens(String string)
    {
        final String OPEN = "${"; //$NON-NLS-1$
        final String CLOSE = "}"; //$NON-NLS-1$

        String newString = string;
        if (newString != null)
        {
            int start = newString.indexOf(OPEN);
            int end = newString.indexOf(CLOSE);

            while (start > -1 && end > start)
            {
                String prepend = newString.substring(0, start);
                String append = newString.substring(end + CLOSE.length());
                String propName = newString.substring(start + OPEN.length(), end);
                String propValue = properties.getProperty(propName);
                if (propValue != null)
                {
                    newString = prepend + propValue + append;
                }

                start = newString.indexOf(OPEN, end);
                end = newString.indexOf(CLOSE, end);
            }
        }

        return newString;
    }

    private void parseCommentGenerator(Context context, Node node)
    {
        CommentGeneratorConfiguration commentGeneratorConfiguration = new CommentGeneratorConfiguration();

        context.setCommentGeneratorConfiguration(commentGeneratorConfiguration);

        Properties attributes = parseAttributes(node);
        String type = attributes.getProperty("type"); //$NON-NLS-1$

        if (stringHasValue(type))
        {
            commentGeneratorConfiguration.setConfigurationType(type);
        }

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            if ("property".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseProperty(commentGeneratorConfiguration, childNode);
            }
        }
    }
}
