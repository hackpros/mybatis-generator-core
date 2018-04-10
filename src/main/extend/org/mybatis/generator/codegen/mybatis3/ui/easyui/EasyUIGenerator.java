package org.mybatis.generator.codegen.mybatis3.ui.easyui;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.text.MessageFormat;
import java.util.List;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.xml.HtmlDocument;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.codegen.AbstractGenerator;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.codegen.mybatis3.ui.AbstractUIGenerator;
import org.mybatis.generator.internal.util.DBInfoUtil;
import org.mybatis.generator.internal.util.JavaBeansUtil;

/**
 *
 * @author fan
 * @see [相关类/方法]（可选）
 * @since p2p_cloud_v1.0
 */
public class EasyUIGenerator extends AbstractUIGenerator
{

	public HtmlDocument getJDLFileDocument() {
		return null;
	}
	
	@Override
	public HtmlDocument getBrowserHtmlDocument(boolean enableTree, boolean enableQueryForm)
	{
		FullyQualifiedJavaType beanType = introspectedTable.getRules().calculateAllFieldsClass();
		Parameter bean = new Parameter(beanType, JavaBeansUtil.getValidPropertyName(beanType.getShortName()));

		HtmlDocument document = new HtmlDocument(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
		        XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);

		FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
		progressCallback.startTask(getString("Progress.12", table.toString())); //$NON-NLS-1$
		TextElement answer = new TextElement("## test"); //$NON-NLS-1$

		answer.setContent("## xxx);");
		answer.setContent("#parse( 'global/header.vm' )");
		StringBuffer jsParamValue = new StringBuffer();
		List<IntrospectedColumn> IntrospectedColumns = introspectedTable.getBaseColumns();
		for (IntrospectedColumn introspectedColumn : IntrospectedColumns)
		{
			String javaProperty = DBInfoUtil.dbName2JavaBeanName(new StringBuilder(introspectedColumn.getJavaProperty()
			        .toLowerCase()));

			String remarks = introspectedColumn.getRemarks();
			boolean enabledCombox = false;
			if (remarks != null && remarks.contains("@"))
			{
				enabledCombox = true;
			}

			if (enabledCombox)
			{
				jsParamValue.append(MessageFormat.format(
				        "&{0}_{1}_DATA=$'{'sof.getValRange(\\\"{0}\\\",\\\"{1}\\\")'}'", bean.getName().toUpperCase(),
				        javaProperty.toUpperCase()));
			}

		}
		answer.setContent("<script type=\"text/javascript\" src=\"$static_path/js/chtcktools/ctdatasource/browser.js?webName=$request.contextPath"
		        + jsParamValue.toString() + "\"></script>");
		if (enableTree)
		{
			answer.setContent("<script type=\"text/javascript\" src=\"$static_path/tree/CSST.UIlib.js\"></script>");
			answer.setContent("<script type=\"text/javascript\" src=\"$static_path/tree/json2.js\"></script>");
			answer.setContent("<link rel=\"stylesheet\" type=\"text/css\" href=\"$static_path/tree/CSST.UIlib.css\" />");
		}

		answer.setContent("<body>");
		if (enableTree)
		{
			answer.setContent("<div id=\"west\" region=\"west\" split=\"true\" title=\"\" style=\"width:180px;\">");
			answer.setContent("</div>");
			answer.setContent("<div region=\"center\" title=\">");
			answer.setContent("     <table id=\"grid\" width=\"350\"></table>");
			answer.setContent("</div>");
		}
		else
		{
			answer.setContent("     <table id=\"grid\" width=\"350\"></table>");
		}

		answer.setContent("</body>");
		answer.setContent("</html>");
		answer.setContent("<script type=\"text/javascript\">");

		if (enableTree)
		{
			answer.setContent(" $(function(){");
			answer.setContent("     tree = new CSST.UI.Tree(\"west\",{children:\"children\",view:\"name\"},${rows});");
			answer.setContent("     tree.children[0].focus();");
			answer.setContent("     tree.onChanged=function(node){");
			answer.setContent("         $('#grid').datagrid({");
			answer.setContent("          url:'doBrowser.json?page=0&rows=10&parentId='+node.data.id+'&ladder='+node.data.ladder");
			answer.setContent("         });     ");
			answer.setContent("     }");
			answer.setContent(" });");
		}

		answer.setContent("</script>");

		document.setTextElement(answer);
		return document;
	}

	@Override
	public HtmlDocument getAppendHtmlDocument()
	{

		FullyQualifiedJavaType beanType = introspectedTable.getRules().calculateAllFieldsClass();
		Parameter bean = new Parameter(beanType, JavaBeansUtil.getValidPropertyName(beanType.getShortName()));

		HtmlDocument document = new HtmlDocument(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
		        XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);

		FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
		progressCallback.startTask(getString("Progress.12", table.toString())); //$NON-NLS-1$
		TextElement answer = new TextElement("## append.vm"); //$NON-NLS-1$
		answer.setContent("");
		answer.setContent("");
		answer.setContent("## @auther:fan");
		answer.setContent("## @date:");

		answer.setContent("#parse( 'global/header.vm' )");
		answer.setContent("<style type=\"text/css\">");
		answer.setContent("     input,textarea{");
		answer.setContent("            width:400px;");
		answer.setContent("            border:1px solid #ccc;");
		answer.setContent("            padding:2px;");
		answer.setContent("         }");
		answer.setContent(" </style>");
		answer.setContent(" <script type=\"text/javascript\">");
		answer.setContent(" </script>");
		answer.setContent("<body >");
		answer.setContent("#parse('global/gobackbar.vm' )");

		answer.setContent("div region=\"center\" class=\"easyui-panel\" title=\"添加\" tools=\"#gobackbar\"style=\"width:500px;margin:0px auto;\">");
		answer.setContent(MessageFormat.format("<form id=\"{0}Frm\" method=\"post\">", bean.getName()));
		answer.setContent("<table>");

		List<IntrospectedColumn> IntrospectedColumns = introspectedTable.getBaseColumns();
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

			String required = "false";
			if (!introspectedColumn.isNullable())
			{
				required = "true";
			}

			int maxLength = introspectedColumn.getLength();

			answer.setContent("<tr>");
			answer.setContent(MessageFormat.format("<td>{0}:</td>", comments));
			answer.setContent("<td>");
			if (enabledCombox)
			{
				answer.setContent(MessageFormat
				        .format("<select id=\"{0}\" name=\"{0}\" class=\"easyui-combobox\" style=\"width:200px;\" required=\"{1}\" editable=\"false\"></select>",
				                javaProperty, required));
			}
			else if (introspectedColumn.getJdbcTypeName().equalsIgnoreCase("date"))
			{
				answer.setContent(MessageFormat.format(
				        "<input id=\"{0}\" name=\"{0}\" value=\"\" class=\"asyui-datetimebox\"  required==\"{1}\"/>",
				        javaProperty, required));
			}
			else if (introspectedColumn.getJdbcTypeName().equalsIgnoreCase("number"))
			{
				answer.setContent(MessageFormat
				        .format("<input id=\"{0}\" name=\"{0}\" value=\"\" class=\"easyui-numberbox\"  required==\"{1}\" validType=\"length[1,{2}]\" precision=\"\" maxLength={2}/>",
				                javaProperty, required, maxLength));
			}
			else
			{
				if (maxLength < 64)
				{
					answer.setContent(MessageFormat
					        .format("<input id=\"{0}\" name=\"{0}\" value=\"\" class=\"easyui-validatebox\"  required==\"{1}\" maxLength={2} />",
					                javaProperty, required, maxLength));
				}
				else
				{
					answer.setContent(MessageFormat
					        .format("<textarea id=\"{0}\" name=\"{0}\" class='comments' onpropertychange='this.style.posHeight=this.scrollHeight'></textarea>",
					                javaProperty));
				}

			}
			answer.setContent("</td>");
			answer.setContent("</tr>");

		}
		answer.setContent("</table>");
		answer.setContent("</form>");
		answer.setContent("<div style=\"text-align:center;padding:5px\">");
		answer.setContent("<a href=\"javascript:void(0)\" id=\"append\" class=\"easyui-linkbutton\">添加</a>");
		answer.setContent("<a href=\"javascript:void(0)\" id=\"clear\" class=\"easyui-linkbutton\"  >重置</a>");
		answer.setContent("</div>");
		answer.setContent("</div>");
		answer.setContent("</body>");
		answer.setContent("</html>");

		// ajax添加
		answer.setContent("<script type=\"text/javascript\">");
		answer.setContent("$(function(){");
		answer.setContent("/********select 选项表**********/");
		for (IntrospectedColumn introspectedColumn : IntrospectedColumns)
		{
			String javaProperty = DBInfoUtil.dbName2JavaBeanName(new StringBuilder(introspectedColumn.getJavaProperty()
			        .toLowerCase()));
			String remarks = introspectedColumn.getRemarks();
			boolean enabledCombox = false;
			String comments = "";
			if (remarks == null || remarks.equals(""))
			{
				comments = introspectedColumn.getActualColumnName();
			}
			else if (remarks.contains("@"))
			{
				comments = comments.split("@")[0];
				enabledCombox = true;
			}
			if (enabledCombox)
			{
				answer.setContent(MessageFormat.format("\t$(\"#{0}\").combobox('{'", javaProperty));
				// data:${define}{sof.getKeyRange("${beanVarName.toUpperCase()}","${col.colName.toUpperCase()}")},
				answer.setContent(MessageFormat.format("\t\tdata:$'{'sof.getKeyRange(\"{0}\",\"{1}\")},", bean
				        .getName().toUpperCase(), javaProperty.toUpperCase()));
				answer.setContent("\t\tvalueField:'id',");
				answer.setContent("\t\ttextField:'text'");
				answer.setContent("\t});");

			}
		}
		answer.setContent("/*********select**************/");

		answer.setContent("//提交");
		answer.setContent("$('#append').click(function()'{'");
		answer.setContent(MessageFormat.format("    if (!$(\"#{0}Frm\").form(\"validate\"))", bean.getName()));
		answer.setContent("         return; ");
		answer.setContent(" }");
		answer.setContent("});");

		answer.setContent("var aj = $.ajax( {");
		answer.setContent(MessageFormat.format("    url : \"$request.contextPath/{0}/doAppend.json\",// 跳转到 action ",
		        bean.getName()));
		answer.setContent(MessageFormat.format("    data : $(\"#{0}Frm\").serialize(),", bean.getName()));
		answer.setContent("     type : 'post',");
		answer.setContent("     dataType:'text',");
		answer.setContent("     success : function(data) {");
		answer.setContent("         if (data.code!=0){");
		answer.setContent("             $.messager.alert('提示','添加成功!','info');");
		answer.setContent(MessageFormat.format(
		        "               $(\"#{0}\",parent.document.body).attr(\"src\",\"$request.contextPath/{0}/browser\");",
		        bean.getName()));
		answer.setContent("         }else{");
		answer.setContent("             $.messager.alert('提示','添加失败!','info');");
		answer.setContent("         }");
		answer.setContent("     },");
		answer.setContent("     error : function() {");
		answer.setContent("     }");
		answer.setContent(" });");
		answer.setContent("});");
		answer.setContent("//重置");
		answer.setContent("$('#clear').click(function(){");
		answer.setContent(MessageFormat.format("$(\"#{0}Frm\")[0].reset();", bean.getName()));
		answer.setContent("});");
		answer.setContent("// clear");
		answer.setContent(" $('#clear').click(function(){");
		answer.setContent(MessageFormat.format("$(\"#{0}Frm\")[0].reset();", bean.getName()));
		answer.setContent("});");
		answer.setContent("// 返回");
		answer.setContent("$('#goback').click(function(){");
		answer.setContent("location.href='browser';");
		answer.setContent("});");
		answer.setContent("});");
		answer.setContent("</script>");

		document.setTextElement(answer);
		return document;
	}

	@Override
	public HtmlDocument getModifyHtmlDocument()
	{
		FullyQualifiedJavaType beanType = introspectedTable.getRules().calculateAllFieldsClass();
		Parameter bean = new Parameter(beanType, JavaBeansUtil.getValidPropertyName(beanType.getShortName()));

		HtmlDocument document = new HtmlDocument(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
		        XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);

		FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
		progressCallback.startTask(getString("Progress.12", table.toString())); //$NON-NLS-1$
		TextElement answer = new TextElement("## modify.vm"); //$NON-NLS-1$
		answer.setContent("");
		answer.setContent("");
		answer.setContent("## @auther:fan");
		answer.setContent("## @date:");

		answer.setContent("#parse( 'global/header.vm' )");
		answer.setContent("<style type=\"text/css\">");
		answer.setContent("     input,textarea{");
		answer.setContent("            width:400px;");
		answer.setContent("            border:1px solid #ccc;");
		answer.setContent("            padding:2px;");
		answer.setContent("         }");
		answer.setContent(" </style>");
		answer.setContent(" <script type=\"text/javascript\">");
		answer.setContent(" </script>");
		answer.setContent("<body >");
		answer.setContent("#parse('global/gobackbar.vm' )");

		answer.setContent("div region=\"center\" class=\"easyui-panel\" title=\"修改\" tools=\"#gobackbar\"style=\"width:500px;margin:0px auto;\">");
		for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns())
		{

			String javaProperty = DBInfoUtil.dbName2JavaBeanName(new StringBuilder(introspectedColumn.getJavaProperty()
			        .toLowerCase()));

			answer.setContent(MessageFormat.format(
			        "<input type=\"hidden\" id=\"{0}\" name=\"{0}\" value=\"${1}.{0}\" />", javaProperty,
			        bean.getName()));
		}
		answer.setContent(MessageFormat.format("<form id=\"{0}Frm\" method=\"post\">", bean.getName()));
		answer.setContent("<table>");

		List<IntrospectedColumn> IntrospectedColumns = introspectedTable.getBaseColumns();
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

			String required = "false";
			if (!introspectedColumn.isNullable())
			{
				required = "true";
			}

			int maxLength = introspectedColumn.getLength();

			answer.setContent("<tr>");
			answer.setContent(MessageFormat.format("<td>{0}:</td>", comments));
			answer.setContent("<td>");
			if (enabledCombox)
			{
				answer.setContent(MessageFormat
				        .format("<select id=\"{0}\" name=\"{0}\" class=\"easyui-combobox\" style=\"width:200px;\" required=\"{1}\" editable=\"false\"></select>",
				                javaProperty, required));

			}
			else if (introspectedColumn.getJdbcTypeName().equalsIgnoreCase("date"))
			{
				answer.setContent(MessageFormat
				        .format("<input id=\"{0}\" name=\"{0}\" value=\"$!'{'{2}.{0}'}'\" class=\"asyui-datetimebox\"  required==\"{1}\"/>",
				                javaProperty, required, bean.getName()));
			}
			else if (introspectedColumn.getJdbcTypeName().equalsIgnoreCase("number"))
			{
				answer.setContent(MessageFormat
				        .format("<input id=\"{0}\" name=\"{0}\" value=\"\" value=\"$!'{'{2}.{0}'}'\" class=\"easyui-numberbox\"  required==\"{1}\" validType=\"length[1,{2}]\" precision=\"\" maxLength={2}/>",
				                javaProperty, required, maxLength, bean.getName()));
			}
			else
			{
				if (maxLength < 64)
				{
					answer.setContent(MessageFormat
					        .format("<input id=\"{0}\" name=\"{0}\" value=\"$!'{'{3}.{0}'}'\" class=\"easyui-validatebox\"  required==\"{1}\" maxLength={2} />",
					                javaProperty, required, maxLength, bean.getName()));
				}
				else
				{
					answer.setContent(MessageFormat
					        .format("<textarea id=\"{0}\" name=\"{0}\" class='comments' onpropertychange='this.style.posHeight=this.scrollHeight'>$!'{'{2}.{0}'}'</textarea>",
					                javaProperty, bean.getName()));
				}

			}
			answer.setContent("</td>");
			answer.setContent("</tr>");

		}
		answer.setContent("</table>");
		answer.setContent("</form>");
		answer.setContent("<div style=\"text-align:center;padding:5px\">");
		answer.setContent("<a href=\"javascript:void(0)\" id=\"append\" class=\"easyui-linkbutton\">添加</a>");
		answer.setContent("<a href=\"javascript:void(0)\" id=\"clear\" class=\"easyui-linkbutton\"  >重置</a>");
		answer.setContent("</div>");
		answer.setContent("</div>");
		answer.setContent("</body>");
		answer.setContent("</html>");

		// ajax添加
		answer.setContent("<script type=\"text/javascript\">");
		answer.setContent("$(function(){");
		answer.setContent("/********select 选项表**********/");
		for (IntrospectedColumn introspectedColumn : IntrospectedColumns)
		{
			String javaProperty = DBInfoUtil.dbName2JavaBeanName(new StringBuilder(introspectedColumn.getJavaProperty()
			        .toLowerCase()));
			String remarks = introspectedColumn.getRemarks();
			boolean enabledCombox = false;
			String comments = "";
			if (remarks == null || remarks.equals(""))
			{
				comments = introspectedColumn.getActualColumnName();
			}
			else if (remarks.contains("@"))
			{
				comments = comments.split("@")[0];
				enabledCombox = true;
			}
			if (enabledCombox)
			{
				answer.setContent("//填充");
				answer.setContent(MessageFormat.format("    $(\"#{0}\").combobox('{'", javaProperty));
				// data:${define}{sof.getKeyRange("${beanVarName.toUpperCase()}","${col.colName.toUpperCase()}")},
				answer.setContent(MessageFormat.format("        data:$'{'sof.getKeyRange(\"{0}\",\"{1}\")},", bean
				        .getName().toUpperCase(), javaProperty.toUpperCase()));
				answer.setContent("     tvalueField:'id',");
				answer.setContent("     textField:'text'");
				answer.setContent(" });");
				answer.setContent("//定位");
				answer.setContent(MessageFormat.format(" $(\"#{1}\").combobox(\"setValue\",$!'{'{0}.{1}'}');",
				        bean.getName(), javaProperty));
				answer.setContent("");
			}
		}
		answer.setContent("/*********select**************/");

		answer.setContent("//提交");
		answer.setContent("$('#append').click(function()'{'");
		answer.setContent(MessageFormat.format("    if (!$(\"#{0}Frm\").form(\"validate\"))", bean.getName()));
		answer.setContent("         return; ");
		answer.setContent(" }");
		answer.setContent("});");

		answer.setContent("var aj = $.ajax( {");
		answer.setContent(MessageFormat.format("    url : \"$request.contextPath/{0}/doAppend.json\",// 跳转到 action ",
		        bean.getName()));
		answer.setContent(MessageFormat.format("    data : $(\"{0}Frm\").serialize(),", bean.getName()));
		answer.setContent("     type : 'post',");
		answer.setContent("     dataType:'text',");
		answer.setContent("     success : function(data) {");
		answer.setContent("         if (data.code!=0){");
		answer.setContent("             $.messager.alert('提示','添加成功!','info');");
		answer.setContent(MessageFormat.format(
		        "               $(\"#{0}\",parent.document.body).attr(\"src\",\"$request.contextPath/{0}/browser\");",
		        bean.getName()));
		answer.setContent("         }else{");
		answer.setContent("             $.messager.alert('提示','添加失败!','info');");
		answer.setContent("         }");
		answer.setContent("     },");
		answer.setContent("     error : function() {");
		answer.setContent("     }");
		answer.setContent(" });");
		answer.setContent("});");
		answer.setContent("//重置");
		answer.setContent("$('#clear').click(function(){");
		answer.setContent(MessageFormat.format("$(\"#{0}Frm\")[0].reset();", bean.getName()));
		answer.setContent("});");
		answer.setContent("// clear");
		answer.setContent(" $('#clear').click(function(){");
		answer.setContent(MessageFormat.format("$(\"#{0}Frm\")[0].reset();", bean.getName()));
		answer.setContent("});");
		answer.setContent("// 返回");
		answer.setContent("$('#goback').click(function(){");
		answer.setContent("location.href='browser';");
		answer.setContent("});");
		answer.setContent("});");
		answer.setContent("</script>");

		document.setTextElement(answer);
		return document;
	}

	@Override
	public HtmlDocument getQueryFormHtmlDocument()
	{
		HtmlDocument document = new HtmlDocument(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
		        XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);

		FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
		progressCallback.startTask(getString("Progress.12", table.toString())); //$NON-NLS-1$
		TextElement answer = new TextElement("## test"); //$NON-NLS-1$

		answer.setContent("## xxx);");
		answer.setContent("#parse( 'global/header.vm' )");
		answer.setContent("<script type=\"text/javascript\" src='$static_path/js/chtcktools/ctdatasource/browser.js?webName=$request.contextPath&&CTDATASOURCE_STATUS_DATA=${sof.getValRange('CTDATASOURCE','STATUS')}'></script>");
		answer.setContent("<body>");
		answer.setContent("    <table id=\"grid\" width=\"350\"></table>");
		answer.setContent("</body>");
		answer.setContent("</html>");
		answer.setContent("<script type=\"text/javascript\">");
		answer.setContent("</script>");

		document.setTextElement(answer);
		return document;
	}

	

	protected void initializeAndExecuteGenerator(AbstractGenerator javaScripGenerator)
	{
		javaScripGenerator.setContext(context);
		javaScripGenerator.setIntrospectedTable(introspectedTable);
		javaScripGenerator.setProgressCallback(progressCallback);
		javaScripGenerator.setWarnings(warnings);

	}


	@Override
    public HtmlDocument getDocument()
    {
		FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
		progressCallback.startTask(getString("Progress.12", table.toString())); //$NON-NLS-1$

		AbstractUIGenerator javaScripGenerator = new JavaScripGenerator();
		initializeAndExecuteGenerator(javaScripGenerator);
		HtmlDocument document = javaScripGenerator.getDocument();
		return document;

    }
}
