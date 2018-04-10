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
public class DoAppendCtlMethodGenerator extends AbstractJavaControlGenerator
{

    public final static String THIS_METHOD_NAME = "doAppend";

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

    public DoAppendCtlMethodGenerator()
    {
        super(true);

    }

    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator()
    {
        // TODO Auto-generated method stub
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

        // 方法内容
        method.addBodyLine(MessageFormat.format("model.addAttribute(\"code\", {0}.insert({1}));",
                fildService.getName(), bean.getName()));
        method.addBodyLine("return \"/browser\";");

        if (context.getPlugins().clientCountByExampleMethodGenerated(method, topLevelClass, introspectedTable))
        {
            topLevelClass.addMethod(method);
        }

    }

    public void addAnnotations(TopLevelClass topLevelClass, Method method)
    {
        //method.addJavaDocLine("/**"); //$NON-NLS-1$
        //method.addJavaDocLine(" *添加"); //$NON-NLS-1$
        //method.addJavaDocLine(" */"); //$NON-NLS-1$
        method.addAnnotation("@RequestMapping(value = \"/doAppend\")");
    }

    @Override
    public List<CompilationUnit> getCompilationUnits()
    {
        // TODO Auto-generated method stub
        return null;
    }
}