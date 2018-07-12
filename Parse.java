package cert;

import java.io.*;
import java.util.ArrayList;

public class Parse {

	public static void main(String[] args) {
		ArrayList<String> names = new ArrayList<>();
		names.add("Z:\\_MultiMedia\\Windows Server 2012 R2 Datacenter");
		names.add("Z:\\_MultiMedia\\Windows Server 2008 R2 Standard");
		names.add("Z:\\_MultiMedia\\Windows Server 2008 R2 Enterprise");
		names.add("Z:\\_MultiMedia\\Windows Server 2003");

		for (int i = 0; i < names.size(); i++) {
			new_cert(names.get(i) + ".txt", names.get(i) + ".bat");
		}
		bat(names, "Z:\\_MultiMedia\\exe.bat");
	}

	public static void new_cert(String name_file, String name_out) {
		ArrayList<String> name_c = new ArrayList<>();
		try (FileReader reader = new FileReader(name_file)) {
			int c;
			String str = new String();
			while ((c = reader.read()) != -1) {
				if (c == '\n') {
					name_c.add(str);
					str = "";
					continue;
				}
				str += ((char) c);
				if (c == '#')
					break;
			}
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		try (FileWriter writer = new FileWriter(name_out, false)) {
			for (int i = 0; i < name_c.size(); i++) {
				if (name_c.get(i).contains("CN")) {
					int beg = name_c.get(i).indexOf("=") + 1;
					int end = name_c.get(i).indexOf(",");
					String s = name_c.get(i).substring(beg, end);
					String b = "openssl genrsa -out Key\\" + s + ".key 4096\r\n" + "openssl req -new -key Key\\" + s
							+ ".key -subj /C=RU/L=Perm/CN=end_user_"+ s +" -out " + s + ".csr\r\n" + "openssl x509 -req -in "
							+ s + ".csr -CA CRT\\middle.crt -CAkey Key\\middle.key -CAcreateserial -out CRT\\" + s
							+ ".crt -days 3\r\n";
					b += "del " + s + ".csr\n\n";
					writer.write(b);
					writer.flush();
				}
			}
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public static void bat(ArrayList<String> names, String name_out) {
		String txt = new String();
		for (int i = 0; i < names.size(); i++) {
			try (FileReader reader = new FileReader(names.get(i) + ".bat")) {
				int c;
				while ((c = reader.read()) != -1) {
					txt += (char) c;
				}
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}
		try (FileWriter writer = new FileWriter(name_out, false)) {
			writer.write(txt);
			writer.flush();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
}