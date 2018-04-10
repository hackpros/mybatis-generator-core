package org.mybatis.generator.codegen.mybatis3.control.elements;

import java.text.MessageFormat;
import java.util.List;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.control.AbstractJavaControlGenerator;
import org.mybatis.generator.internal.util.JavaBeansUtil;

/**
 * 〈一句话功能简述〉
 * 〈功能详细描述〉
 *
 * @author fan
 * @see [相关类/方法]（可选）
 * @since p2p_cloud_v1.0
 */
public class DoBrowserCtlMethodGenerator extends AbstractJavaControlGenerator
{

    public final static String THIS_METHOD_NAME = "doBrowser";

    public Parameter bean;

    public Field fildService;

    public void init()
    {
        FullyQualifiedJavaType beanType = introspectedTable.getRules().calculateAllFieldsClass();
        bean = new Parameter(beanType, JavaBeansUtil.getValidPropertyName(beanType.getShortName()));

        FullyQualifiedJavaType fieldType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3DbxServiceType());
        fildService = new Field(JavaBeansUtil.firstLetterLower(fieldType.getShortName()),
                new FullyQualifiedJavaType(fieldType.getShortName()));
    }
    public DoBrowserCtlMethodGenerator()
    {
        super(true);
    }

    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator()
    {
        return null;
    }

    @Override
    public void addMethod(TopLevelClass topLevelClass)
    {
        Method method = new Method();
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setName(THIS_METHOD_NAME);
        method.setVisibility(JavaVisibility.PUBLIC);
        // 给方法加注释
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        // 给方法添加注解
        addAnnotations(topLevelClass, method);

        // spring model　参数
        FullyQualifiedJavaType modelType = new FullyQualifiedJavaType("Model");
        Parameter model = new Parameter(modelType, JavaBeansUtil.getValidPropertyName(modelType.getShortName()));
        method.addParameter(model);

        // 对象参数
        method.addParameter(bean);
        // 分页参数
        Parameter param= new Parameter(FullyQualifiedJavaType.getIntInstance(), "page");
        param.addAnnotation("@RequestParam(value = \"page\", required = false)");
        method.addParameter(param);
        method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "rows"));

        // 方法内容
        method.addBodyLine("page = page <= 1 ? 0 : page - 1;");
        method.addBodyLine("rows = rows <= 0 ? 10 : rows;");
        method.addBodyLine("page *= rows;");
        method.addBodyLine("rows += page;");
        method.addBodyLine(MessageFormat.format("Pages<{0}> pages = {1}.browser({2}, page, rows);",bean.getType().getShortName(),fildService.getName(),bean.getName()));
        method.addBodyLine("model.addAttribute(\"total\", pages.getTotal());");
        method.addBodyLine("model.addAttribute(\"rows\", pages.getRows());");

        method.addBodyLine(MessageFormat.format("return \"/{0}/browser\";", bean.getName()));

        if (context.getPlugins().clientCountByExampleMethodGenerated(method, topLevelClass, introspectedTable))
        {
            topLevelClass.addMethod(method);
        }
    }

    @Override
    public List<CompilationUnit> getCompilationUnits()
    {
        return null;

    }

    public void addAnnotations(TopLevelClass topLevelClass, Method method)
    {
        //method.addJavaDocLine("/**"); //$NON-NLS-1$
        //method.addJavaDocLine(" *添加"); //$NON-NLS-1$
        //method.addJavaDocLine(" */"); //$NON-NLS-1$
        method.addAnnotation("@RequestMapping(value = \"/doBrowser\")");
    }
}