import org.mybatis.generator.api.ShellRunner;


public class StartTest {

	
	public static void main(String[] args) {
		
		String config = StartTest.class.getResource("/test.xml").getFile();
		//String config = MsqlStartTest.class.getResource("/mysql_base.xml").getFile();
		String[] arg = { "-configfile", config, "-overwrite" };
		ShellRunner.main(arg);
	}

	
	
}
