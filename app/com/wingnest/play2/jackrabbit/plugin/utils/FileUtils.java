/*
 * Copyright since 2013 Shigeru GOUGI (sgougi@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wingnest.play2.jackrabbit.plugin.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {

	public static void deepDelete(final File file) throws IOException {
		if ( !file.exists() )
			return;
		if ( file.isDirectory() ) {
			if ( file.list().length == 0 ) {
				file.delete();
			} else {
				final String files[] = file.list();
				for ( final String temp : files ) {
					final File fileDelete = new File(file, temp);
					deepDelete(fileDelete);
				}
				if ( file.list().length == 0 ) {
					file.delete();
				}
			}
		} else {
			file.delete();
		}
	}

	private FileUtils() {
	}

}
