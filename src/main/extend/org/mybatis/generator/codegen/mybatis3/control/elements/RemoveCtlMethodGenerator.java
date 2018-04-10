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
 *
 * @author fan
 * @see [相关类/方法]（可选）
 * @since p2p_cloud_v1.0
 */
public class RemoveCtlMethodGenerator extends AbstractJavaControlGenerator
{

    public final static String THIS_METHOD_NAME = "remove";

    public Parameter bean;

    public Parameter Service;

    public Field fildService;

    public void init()
    {
        FullyQualifiedJavaType beanType = new FullyQualifiedJavaType(
                "java.unit.List<" + introspectedTable.getRules().calculateAllFieldsClass() + ">"); //$NON-NLS-1$
        bean = new Parameter(beanType, "list");

        FullyQualifiedJavaType fieldType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3DbxServiceType());
        fildService = new Field(JavaBeansUtil.firstLetterLower(fieldType.getShortName()),
                new FullyQualifiedJavaType(fieldType.getShortName()));
    }

    public RemoveCtlMethodGenerator()
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
        bean.addAnnotation("@RequestBody");
        method.addParameter(bean);
        // 分页参数　

        // 方法内容
        method.addBodyLine(MessageFormat.format("model.addAttribute(\"{1}\", {0}.delete({1}));", fildService.getName(),
                bean.getName()));
        method.addBodyLine(MessageFormat.format("return \"/{0}/browser\";", bean.getName()));

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
        method.addAnnotation("@RequestMapping(value = \"/delete\")");
    }

    @Override
    public List<CompilationUnit> getCompilationUnits()
    {
        // TODO Auto-generated method stub
        return null;
    }
}