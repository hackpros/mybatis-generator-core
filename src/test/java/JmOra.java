import org.mybatis.generator.api.ShellRunner;


public class JmOra {

	
	public static void main(String[] args) {
		
		String config = JmOra.class.getResource("/jmOra.xml").getFile();
		//String config = MsqlStartTest.class.getResource("/mysql_base.xml").getFile();
		String[] arg = { "-configfile", config, "-overwrite" };
		ShellRunner.main(arg);
	}

	
	
}
