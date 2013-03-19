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
package com.wingnest.play2.jackrabbit.plugin;

import org.apache.jackrabbit.ocm.reflection.ReflectionUtils;

import com.wingnest.play2.jackrabbit.Jcr;
import com.wingnest.play2.jackrabbit.Jcr.RawStuff;
import com.wingnest.play2.jackrabbit.plugin.director.DefaultDirector;
import com.wingnest.play2.jackrabbit.plugin.director.Director;
import com.wingnest.play2.jackrabbit.plugin.manager.JackrabbitManager;
import com.wingnest.play2.jackrabbit.plugin.manager.Manager;

import play.Application;
import play.Play;
import play.Plugin;

final public class JackrabbitPlugin extends Plugin {

	private static Director DIRECTOR = null;
	
	public JackrabbitPlugin(final Application application) {
	}
	
	@Override
	public void onStart() {
		ReflectionUtils.setClassLoader(Play.application().classloader());		
		if ( DIRECTOR == null ) {
			DIRECTOR = new DefaultDirector(new JackrabbitManager());
			Jcr.setRawStuff(new RawStuff() {
				@Override
				public Manager getManager() {
					return DIRECTOR.getManager();
				}
			});
		}
		
		DIRECTOR.onApplicationStart();
	}

	@Override
	public void onStop() {
		if ( DIRECTOR != null) {
			DIRECTOR.onApplicationStop();
			DIRECTOR = null;
		}
	 }	
}
