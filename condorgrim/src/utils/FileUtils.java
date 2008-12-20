package utils;

import java.io.File;

public class FileUtils {
	public static void cleanUpDirectory(String directory) {
		File dir = new File(directory);

		String[] children = dir.list();
		if (children == null) {
			// Either dir does not exist or is not a directory
		} else {
			for (int i = 0; i < children.length; i++) {
				File file = new File(directory,	children[i]);
				System.out.print(file + "  deleted : " + file.delete());
			}
		}
	}
}
