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

import java.util.HashSet;
import java.util.Set;

import play.Application;

import com.wingnest.play2.jackrabbit.plugin.exceptions.JackRabbitUnexpectedException;

final public class TypeUtils {

	public static String getTypeName(final Class<?> clazz) {
		return clazz.getSimpleName();
	}

	public static String getTypeNameForProperty(final Class<?> clazz) {
		final String name = getTypeName(clazz);
		return new StringBuffer().append(name.substring(0, 1).toLowerCase()).append(name.substring(1)).toString();
	}
	

	@SuppressWarnings("unchecked")
	public static <T> Set<Class<T>> getSubTypesOf(final Application application, final String packageName, final Class<T> clazz) {
		final Set<Class<T>> classes = new HashSet<Class<T>>();
		try {
			final Set<String> classNames = new HashSet<String>();
			classNames.addAll(play.libs.Classpath.getTypes(application, packageName));
			for ( final String clazzName : classNames ) {
				final Class<?> c = Class.forName(clazzName, true, application.classloader());
				if ( clazz != null && clazz.isAssignableFrom(c) )
					classes.add((Class<T>) c);
				else
					classes.add((Class<T>) c);
			}
		} catch ( Exception e ) {
			throw new JackRabbitUnexpectedException(e);
		}
		return classes;
	}
	
	
	private TypeUtils() {
	}

}
