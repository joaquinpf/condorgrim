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
	 * @param f the directory
	 */
	public static void cleanUpDirectory(File f) {

		String[] children = f.list();
		if (children == null) {
			// Either dir does not exist or is not a directory
		} else {
			for (int i = 0; i < children.length; i++) {
				File file = new File(f, children[i]);
				System.out.print(file + "  deleted : " + file.delete());
			}
		}
	}
}

