package org.mybatis.generator.codegen.mybatis3.ui;

import org.mybatis.generator.api.dom.xml.HtmlDocument;
import org.mybatis.generator.codegen.AbstractGenerator;

/**
 *
 * @author fan
 * @see [相关类/方法]（可选）
 * @since p2p_cloud_v1.0
 */
public abstract class AbstractUIGenerator extends AbstractGenerator
{
    /**
     * 生成查询页面
     */
    public final static String ENABLE_BROWSER = "enableBrowser";

    /**
     * 生成添加页面
     */
    public final static String ENABLE_APPEND = "enableAppend";

    /**
     * 生成修改页面
     */
    public final static String ENABLE_MODIFY = "enableModify";

    /**
     * 查询页面生成树
     */
    public final static String ENABLE_TREE = "enableTree";

    /**
     * tg 生成查询的form表单
     */
    public final static String ENABLE_QUERYFORM = "enableQueryForm";
    /**
     * 生成js
     */
    public final static String JAVASCRIPT_PACKAGE = "javaScriptPackage";

    public AbstractUIGenerator()
    {
        super();
    }

    /**
     * 生成browser页面
     */
    public abstract HtmlDocument getBrowserHtmlDocument(boolean enableTree, boolean enableQueryForm);
    /**
     * 生成append页面
     */
    public abstract HtmlDocument getAppendHtmlDocument();
    /**
     * 生成modify页面
     */
    public abstract HtmlDocument getModifyHtmlDocument();
    /**
     * 生成queryform页面
     */
    public abstract HtmlDocument getQueryFormHtmlDocument();
    
    /**
     * 生成curdjs文件
     */
    public abstract HtmlDocument getDocument();
    /**
     * 生成jdl
     */
	public abstract HtmlDocument getJDLFileDocument();
}
