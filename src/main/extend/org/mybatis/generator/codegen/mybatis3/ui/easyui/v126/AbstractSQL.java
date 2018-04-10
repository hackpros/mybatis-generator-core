package org.mybatis.generator.codegen.mybatis3.ui.easyui.v126;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.xml.HtmlDocument;
import org.mybatis.generator.api.dom.xml.TextElement;

/**
 *
 * @author fan
 * @see [相关类/方法]（可选）
 * @since p2p_cloud_v1.0
 */
public abstract class AbstractSQL<T>
{

	// private static final String AND = ") \nAND (";

	// private static final String OR = ") \nOR (";

	public abstract T getSelf();

	public T addMain(Method method)
	{
		sql().main.add(method);
		return getSelf();
	}

	public T addElement(Field fld)
	{
		sql().element.add(fld);
		return getSelf();
	}

	public T addDataGrid(JavaElement element)
	{
		sql().dataGrid.add(element);
		return getSelf();
	}

	public HtmlDocument toDocument()
	{
		StringBuilder sb = new StringBuilder();
		sql().sql(sb);
		HtmlDocument document = new HtmlDocument();
		document.setTextElement(new TextElement(sb.toString()));
		return document;
	}

	private SQLStatement sql = new SQLStatement();

	private SQLStatement sql()
	{
		return sql;
	}

	public <A extends Appendable> A usingAppender(A a)
	{
		sql().sql(a);
		return a;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sql().sql(sb);
		return sb.toString();
	}

	private static class SafeAppendable
	{
		private final Appendable a;

		private boolean empty = true;

		public SafeAppendable(Appendable a)
		{
			super();
			this.a = a;
		}

		public SafeAppendable append(CharSequence s)
		{
			try
			{
				if (empty && s.length() > 0)
					empty = false;
				a.append(s);
			}
			catch(IOException e)
			{
				throw new RuntimeException(e);
			}
			return this;
		}

		public boolean isEmpty()
		{
			return empty;
		}

	}

	private static class SQLStatement
	{

		List<Method> main = new ArrayList<Method>();

		List<Field> element = new ArrayList<Field>();

		List<JavaElement> dataGrid = new ArrayList<JavaElement>();

		private void sqlClause(SafeAppendable sb)
		{
			if (!main.isEmpty())
			{
				if (!sb.isEmpty())
					sb.append("\n");

				int i = 0;
				for (Method method : main)
				{
					sb.append(method.getFormattedContent(i++, false));
				}
			}

		}

		private void sqlClauseElement(SafeAppendable sb)
		{
			if (!element.isEmpty())
			{
				if (!sb.isEmpty())
					sb.append("\n");

				int i = 0;
				for (Field fld : element)
				{
					sb.append(fld.getFormattedContent(i));
					sb.append("\n");
				}
			}

		}

		private void sqlClauseDataGrid(SafeAppendable sb)
		{
			if (!dataGrid.isEmpty())
			{
				if (!sb.isEmpty())
					sb.append("\n");
				int i = 0;
				for (JavaElement e : dataGrid)
				{
					if (e instanceof Method){
						Method method=(Method) e;
						sb.append(method.getFormattedContent(i++, false));
					}else if(e instanceof Field){
						Field fld=(Field) e;
						sb.append(fld.getFormattedContent(i));
					}
					
					sb.append("\n");
				}
			}
		}

		private void addMain(SafeAppendable builder)
		{
			sqlClause(builder);
		}

		private void element(SafeAppendable builder)
		{
			sqlClauseElement(builder);
		}

		private void datagrid(SafeAppendable builder)
		{

			sqlClauseDataGrid(builder);
		}

		public void sql(Appendable a)
		{
			SafeAppendable builder = new SafeAppendable(a);
			addMain(builder);
			builder.append("\n\r");
			element(builder);
			builder.append("\n\r");
			datagrid(builder);
			builder.append("\n\r");
		}

	}
}
