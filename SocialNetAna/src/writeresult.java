import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class writeresult {
	public static void write_file(String path,String content) throws IOException{
		System.out.println("开始写入文件");
		FileWriter fw = new FileWriter(path, true);
		BufferedWriter bw = new BufferedWriter(fw);

		bw.write(content);
		bw.newLine();
		bw.flush();
	}
}
