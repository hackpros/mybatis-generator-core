package org.mybatis.generator.codegen.mybatis3.ui.jdl;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.xml.HtmlDocument;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.codegen.mybatis3.ui.AbstractUIGenerator;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.internal.util.StringUtility;

/**
 *
 * @author fan
 * @see [相关类/方法]（可选）
 * @since p2p_cloud_v1.0
 */
public class JDLUIGenerator extends AbstractUIGenerator {

	/*
	 * SQL MongoDB Cassandra Validations String String String required,
	 * minlength, maxlength, pattern Integer Integer Integer required, min, max
	 * Long Long Long required, min, max BigDecimal BigDecimal BigDecimal
	 * required, min, max Float Float Float required, min, max Double Double
	 * Double required, min, max Enum Enum required Boolean Boolean Boolean
	 * required LocalDate LocalDate required Date required ZonedDateTime
	 * ZonedDateTime required UUID required Blob Blob required, minbytes,
	 * maxbytes AnyBlob AnyBlob required, minbytes, maxbytes ImageBlob ImageBlob
	 * required, minbytes, maxbytes TextBlob TextBlob required, minbytes,
	 * maxbytes Instant Instant Instant required
	 */

	final String DATE_FIELD_FMT =    "	{0} Instant {1}   /** {2} */";
	final String LONG_FIELD_FMT =    "	{0} Long    {1}  min(0) max({2})  /** {3} */";
	final String INTEGER_FIELD_FMT = "	{0} Integer {1}  min(0) max({2})  /** {3} */";
	final String STRING_FIELD_FMT =  "	{0} String  {1}  minlength(0) maxlength({2})  /** {3} */";
	final String ENUM_FIELD_FMT = "";

	public HtmlDocument getJDLFileDocument() {

		FullyQualifiedJavaType beanType = introspectedTable.getRules().calculateAllFieldsClass();
		Parameter bean = new Parameter(beanType, JavaBeansUtil.getValidPropertyName(beanType.getShortName()));

		HtmlDocument document = new HtmlDocument(XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
				XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);

		FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
		progressCallback.startTask(getString("Progress.12", table.toString())); //$NON-NLS-1$

		
		TextElement answer = new TextElement("/**\n"); //$NON-NLS-1$
		answer.setContent("*"+introspectedTable.getFullyQualifiedTable().getComment());
		answer.setContent("* @auther:renntrabbit@foxmail.com");
		answer.setContent("* @date:"+ new Date());
		answer.setContent("* table:"+ introspectedTable.getTableConfiguration().getTableName());
		answer.setContent("*/");
		answer.setContent(MessageFormat.format("entity {0} ({1})", bean.getType().getShortName(),introspectedTable.getTableConfiguration().getTableName()));
		answer.setContent("{");
		/*JavaTypeResolver javaTypeResolver = ObjectFactory.createJavaTypeResolver(this.context, warnings);*/
		List<IntrospectedColumn> introspectedColumns = introspectedTable.getBaseColumns();
		for (IntrospectedColumn introspectedColumn : introspectedColumns) {

			
			String remarks = introspectedColumn.getRemarks();
			Pattern p = Pattern.compile("\t|\r|\n|,");  
			Matcher m = p.matcher(remarks);  
			remarks = m.replaceAll("");  
			remarks = remarks.replaceAll(" +", " ");  

			
			
			boolean enabledCombox = false;
			String comments = remarks;
			/*if (remarks == null || remarks.equals("")) {
				comments = introspectedColumn.getRemarks();
			} else if (remarks.contains("@")) {
				comments = comments.split("@")[0];
				enabledCombox = true;
			}*/

			if (StringUtility.isEmpty(comments)){
				comments=introspectedColumn.getActualColumnName();
			}
			String required = "";
			if (!introspectedColumn.isNullable()) {
				required = "required";
			}

			int maxLength = introspectedColumn.getLength();

			if (enabledCombox) {
				answer.setContent(MessageFormat.format(DATE_FIELD_FMT, introspectedColumn.getJavaProperty() ,required,maxLength, comments));
			} else if (introspectedColumn.getJdbcTypeName().equalsIgnoreCase("date") 
					 || introspectedColumn.getJdbcTypeName().equalsIgnoreCase("datetime")
					 || introspectedColumn.getJdbcTypeName().equalsIgnoreCase("timestamp")
					) {
				answer.setContent(MessageFormat.format(DATE_FIELD_FMT, introspectedColumn.getJavaProperty(),  required, comments));
			} else if (introspectedColumn.getJdbcTypeName().equalsIgnoreCase("number")) {
				answer.setContent(MessageFormat.format(LONG_FIELD_FMT, introspectedColumn.getJavaProperty(), required, maxLength, comments));
			} else if (introspectedColumn.getJdbcTypeName().equalsIgnoreCase("integer")) {
				answer.setContent(MessageFormat.format(INTEGER_FIELD_FMT, introspectedColumn.getJavaProperty(), required, maxLength, comments));
			} else {
				answer.setContent(MessageFormat.format(STRING_FIELD_FMT, introspectedColumn.getJavaProperty(), required, maxLength, comments));
			}
			
			
		}
		answer.removeLastChar();
		answer.setContent("}");
		
		
		answer.setContent(MessageFormat.format("service  {0} with serviceClass",bean.getType().getShortName()));
		answer.setContent(MessageFormat.format("service  {0} with serviceImpl",bean.getType().getShortName()));
		answer.setContent(MessageFormat.format("paginate {0} with pager",bean.getType().getShortName()));
		answer.setContent(MessageFormat.format("dto      {0} with mapstruct",bean.getType().getShortName()));
		
		document.setTextElement(answer);
		return document;
	}

	@Override
	public HtmlDocument getBrowserHtmlDocument(boolean enableTree, boolean enableQueryForm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HtmlDocument getAppendHtmlDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HtmlDocument getModifyHtmlDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HtmlDocument getQueryFormHtmlDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HtmlDocument getDocument() {
		// TODO Auto-generated method stub
		return null;
	}

}
