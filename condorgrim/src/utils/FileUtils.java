/*
 * 
 */
package utils;

import java.io.File;

// TODO: Auto-generated Javadoc
/**
 * The Class FileUtils.
 */
public class FileUtils {

	/**
	 * Clean up directory.
	 * 
	 * @param directory the directory
	 */
	public static void cleanUpDirectory(String directory, String hash) {
		File dir = new File(directory);

		String[] children = dir.list();
		if (children == null) {
			// Either dir does not exist or is not a directory
		} else {
			for (int i = 0; i < children.length; i++) {
				File file = new File(directory, children[i]);
				if (file.getName().contains(hash)) {
					System.out.print(file + "  deleted : " + file.delete());
				}
			}
		}
	}
}
