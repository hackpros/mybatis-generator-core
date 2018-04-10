package org.mybatis.generator.codegen.mybatis3.ui.macui;

import java.text.MessageFormat;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.xml.HtmlDocument;
import org.mybatis.generator.codegen.mybatis3.ui.AbstractUIGenerator;
import org.mybatis.generator.codegen.mybatis3.ui.easyui.v126.SQL;
import org.mybatis.generator.internal.util.DBInfoUtil;
import org.mybatis.generator.internal.util.JavaBeansUtil;

/**
 * 生成js脚本
 * 
 * @author fan
 * @see [相关类/方法]（可选）
 * @since p2p_cloud_v1.0
 */
public class AngularjsGenerator extends AbstractUIGenerator
{
	public HtmlDocument getJDLFileDocument() {
		return null;
	}
	@Override
	public HtmlDocument getBrowserHtmlDocument(boolean enableTree, boolean enableQueryForm)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HtmlDocument getAppendHtmlDocument()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HtmlDocument getModifyHtmlDocument()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public HtmlDocument getQueryFormHtmlDocument()
	{
		return null;
	}

	@Override
	public HtmlDocument getDocument()
	{
		FullyQualifiedJavaType beanType = introspectedTable.getRules().calculateAllFieldsClass();
		Parameter bean = new Parameter(beanType, JavaBeansUtil.getValidPropertyName(beanType.getShortName()));
		FullyQualifiedJavaType emptyType = new FullyQualifiedJavaType("");

		SQL sql = new SQL();
		FullyQualifiedJavaType type = new FullyQualifiedJavaType("var");

		Method method = new Method();
		method.setName("getScriptArg=function");
		method.setReturnType(type);
		method.setVisibility(JavaVisibility.DEFAULT);
		Parameter parameter = new Parameter(emptyType, "key");

		method.addParameter(parameter);
		method.addBodyLine("var scripts=document.getElementsByTagName(\"script\"),");
		method.addBodyLine("script=scripts[scripts.length-2],  ");
		method.addBodyLine("src=script.src; ");
		method.addBodyLine("return (src.match(new RegExp(\"(?:\\?|&)\"+key+\"=(.*?)(?=&|$)\"))||['',null])[1];");
		sql.addMain(method);

		Field fld = new Field("_WEB_NAME=getScriptArg(\"webName\")", type);
		sql.addElement(fld);
		List<IntrospectedColumn> IntrospectedColumns = introspectedTable.getBaseColumns();
		for (IntrospectedColumn introspectedColumn : IntrospectedColumns)
		{
			if (introspectedColumn.getRemarks() != null && introspectedColumn.getRemarks().contains("@"))
			{
				String javaProperty = DBInfoUtil.dbName2JavaBeanName(new StringBuilder(introspectedColumn
				        .getJavaProperty().toLowerCase()));
				String value = "_{0}_{1}_DATA=jQuery.parseJSON(getScriptArg(\"{0}_{1}_DATA\"))";

				fld = new Field(MessageFormat.format(value, bean.getName().toUpperCase(), javaProperty.toUpperCase()),
				        type);
				sql.addElement(fld);
			}

		}

		method = new Method();
		method.setName("$(function");
		method.setVisibility(JavaVisibility.DEFAULT);
		method.setReturnType(emptyType);
		method.addBodyLine("//");
		method.addBodyLine("$('#grid').datagrid({");
		method.addBodyLine("fit : true,");
		method.addBodyLine("pageNumber : 1,");
		method.addBodyLine("pageList : [ 10, 20, 30 ],");
		method.addBodyLine("nowrap : true,");
		method.addBodyLine("striped : true,");
		method.addBodyLine("collapsible : true,");
		method.addBodyLine("remoteSort : false,");
		method.addBodyLine("url:'doBrowser.json',");
		method.addBodyLine("columns : [ [ ");

		IntrospectedColumns = introspectedTable.getAllColumns();
		for (IntrospectedColumn introspectedColumn : IntrospectedColumns)
		{
			String javaProperty = DBInfoUtil.dbName2JavaBeanName(new StringBuilder(introspectedColumn.getJavaProperty()
			        .toLowerCase()));

			String remarks = introspectedColumn.getRemarks();
			boolean enabledCombox = false;
			String comments = remarks;
			if (remarks == null || remarks.equals(""))
			{
				comments = introspectedColumn.getRemarks();
			}
			else if (remarks.contains("@"))
			{
				comments = comments.split("@")[0];
				enabledCombox = true;
			}
			if (enabledCombox)
			{
				method.addBodyLine(MessageFormat.format(
				        "	'{title : \"{0}\",  field : \"{1}\", width : 80,sortable : true", comments, javaProperty));
				method.addBodyLine("		formatter:function(keycode){//");
				method.addBodyLine(MessageFormat.format("		return eval(\"_{0}_{1}_DATA.{0}_{1}_\"+keycode);", bean
				        .getName().toUpperCase(), javaProperty.toUpperCase()));
				method.addBodyLine("		}");
				method.addBodyLine("	},");
			}
			else
			{
				method.addBodyLine(MessageFormat.format(
				        "	'{title : \"{0}\",  field : \"{1}\", width : 80,sortable : true'},", comments, javaProperty));
			}
		}

		method.addBodyLine("] ],");

		method.addBodyLine("pagination : true,");
		method.addBodyLine("singleSelect : true,");
		method.addBodyLine("rownumbers : true,");
		method.addBodyLine("toolbar:[{");
		method.addBodyLine("id:'btnadd',");
		method.addBodyLine("text:'添加',");
		method.addBodyLine("iconCls:'icon-add',");
		method.addBodyLine("handler:function(){");
		method.addBodyLine("location.href=\"append\";");
		method.addBodyLine("}");
		method.addBodyLine("},{");

		method.addBodyLine("id:'btncut',");
		method.addBodyLine("text:'修改',");
		method.addBodyLine("iconCls:'icon-edit',");
		method.addBodyLine("handler:function(){");
		method.addBodyLine("var selected = $('#grid').datagrid('getSelected');");
		method.addBodyLine("if (selected){");
		method.addBodyLine("location.href = 'modify?id='+selected.id+'&name='+selected.id;");
		method.addBodyLine("//$(\"#usrrole\",parent.document.body).attr(\"src\",_WEB_NAME+\"/${beanModeName}/usrRole/modify?id=\"+selected.id);");
		method.addBodyLine("}else{");
		method.addBodyLine("$.messager.alert('提示',\"请选择一条记录\");");
		method.addBodyLine("}");
		method.addBodyLine("}");
		method.addBodyLine("},'-',{");

		method.addBodyLine("id:'btndel',");
		method.addBodyLine("text:'删除',");
		method.addBodyLine("disabled:false,");
		method.addBodyLine("iconCls:'icon-remove',");
		method.addBodyLine("handler:function(){");
		method.addBodyLine("var rows = $('#grid').datagrid('getSelections');");
		method.addBodyLine("if (rows.length==0){");
		method.addBodyLine("$.messager.alert('提示',\"请选择一条记录\");");
		method.addBodyLine("return;");
		method.addBodyLine("}");
		method.addBodyLine("var data = [];");
		method.addBodyLine("for (var i=0 ;i<rows.length;i++){");
		method.addBodyLine("var ids = {\"id\":rows[i].id};");
		method.addBodyLine("data.push(ids);");
		method.addBodyLine("}");
		method.addBodyLine("$.ajax( {");
		method.addBodyLine("contentType : 'application/json',");
		method.addBodyLine("url : 'delete.json',     ");
		method.addBodyLine("data :JSON.stringify(data),");
		method.addBodyLine("type : 'post',");
		method.addBodyLine("dataType:'text',");
		method.addBodyLine("success : function(data) {");
		method.addBodyLine("if (data.id!=0){");
		method.addBodyLine("$.messager.alert('提示','删除成功!','info');");
		method.addBodyLine("}else{");
		method.addBodyLine("$.messager.alert('提示','删除失败!','info');");
		method.addBodyLine("}");
		method.addBodyLine("$('#grid').datagrid(\"reload\");");
		method.addBodyLine("},");
		method.addBodyLine("error : function(data) {");
		method.addBodyLine("console.log(data);");
		method.addBodyLine("$.messager.alert('提示','删除失败!','info');");
		method.addBodyLine("}");
		method.addBodyLine("});");
		method.addBodyLine("}");
		method.addBodyLine("}]");

		method.addBodyLine("});");
		sql.addDataGrid(method);
		fld = new Field(")", emptyType);
		sql.addDataGrid(fld);

		return sql.toDocument();
	}

}
