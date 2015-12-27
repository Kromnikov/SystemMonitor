import com.core.SpringService;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Program {


	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws SQLException {
		long start = System.currentTimeMillis();

		SpringService.run();




		start = System.currentTimeMillis() - start;
		System.out.println("Время выполнения(мс): "+start);
		System.exit(0);
	}

}
