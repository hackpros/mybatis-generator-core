package org.mybatis.generator.internal.util;

public class DBInfoUtil {
    
	private final static String UNDER_LINE = "_";

	public static String firstLetterLower(String name) {
		StringBuilder newWords = new StringBuilder(name);
		newWords.replace(0, 1, newWords.substring(0, 1).toLowerCase());
		return newWords.toString();
	}
	public static String firstLetterUpper(String name) {
		StringBuilder newWords = new StringBuilder(name);
		newWords.replace(0, 1, newWords.substring(0, 1).toUpperCase());
		return newWords.toString();
	}
	/**
	 *  abcd=abcd
	 *	a_bcd=aBcd
	 *	ab_cd=abCd
	 *	abc_d=abcD
	 *	abcd_=abcd
	 *	_abcd=abcd
	 *	_a_bcd_=aBcd
	 *	_abc_d_=abcD
	 *
	 * @param name
	 * @return
	 */
	public static String dbName2JavaBeanName(StringBuilder name) {

		int index = name.indexOf(UNDER_LINE);
		switch (index) {
		case 0:
			return dbName2JavaBeanName(name.delete(0, 1));
		case -1:
			return name.toString();
		default:
			if (index == name.length()-1) {
				return name.delete(index, index+1).toString();
			}
			return dbName2JavaBeanName(name.replace(index, index + 2, name.substring(index+1, index+2).toUpperCase()));
		}
	}

	public static String dbColumn2JavaBeanName(StringBuilder name) {
		System.out.print(name.toString()+"=");
		return dbName2JavaBeanName(name);

	}
	
	public static void main(String[] args) {
		
		StringBuilder a=new StringBuilder("abcd");
		StringBuilder b=new StringBuilder("a_bcd");
		StringBuilder c=new StringBuilder("ab_cd");
		StringBuilder d=new StringBuilder("abc_d");
		StringBuilder e=new StringBuilder("abcd_");
		StringBuilder f=new StringBuilder("_abcd");
		StringBuilder g=new StringBuilder("_a_bcd_");
		StringBuilder h=new StringBuilder("_abc_d_");
		
		System.out.println(dbColumn2JavaBeanName(a));
		System.out.println(dbColumn2JavaBeanName(b));
		System.out.println(dbColumn2JavaBeanName(c));
		System.out.println(dbColumn2JavaBeanName(d));
		System.out.println(dbColumn2JavaBeanName(e));
		System.out.println(dbColumn2JavaBeanName(f));
		System.out.println(dbColumn2JavaBeanName(g));
		System.out.println(dbColumn2JavaBeanName(h));
	}
}
