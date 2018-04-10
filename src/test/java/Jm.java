import org.mybatis.generator.api.ShellRunner;

public class Jm {

	public static void main(String[] args) {

		String config = Jm.class.getResource("/jmore.xml").getFile();
		// String config =
		// MsqlStartTest.class.getResource("/mysql_base.xml").getFile();
		String[] arg = { "-configfile", config, "-overwrite" };
		ShellRunner.main(arg);
	}

}
