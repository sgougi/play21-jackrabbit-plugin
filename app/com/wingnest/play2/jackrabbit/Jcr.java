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
package com.wingnest.play2.jackrabbit;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import com.wingnest.play2.jackrabbit.plugin.manager.Manager;
import com.wingnest.play2.jackrabbit.plugin.manager.Manager.Config;


public class Jcr {

	final private static ThreadLocal<Map<String, Session>> TL_SESSION_MAP = new ThreadLocal<Map<String, Session>>(); 

	public static Config getConfig() {
		return RAW_STUFF.getManager().getConfig();
	}

	public static Repository getRepository() {
		return RAW_STUFF.getManager().getRepository();
	}

	public static Session login(final String user, final String password) throws RepositoryException  {
		Session session = getSessionMap().get(makeSessionMapKey(user, password, null));
		if ( session != null ) return session;		
		try {
			session = getRepository().login(new SimpleCredentials(user, password.toCharArray()), null);
			if ( session != null ) {
				getSessionMap().put(makeSessionMapKey(user, password, null), session);
			}			
			return session;
		} catch ( Exception e ) {
			throw new RepositoryException("Impossible to login ", e);
		}
	}

	public static Session login(final String user, final String password, final String workspace) throws RepositoryException  {
		Session session = getSessionMap().get(makeSessionMapKey(user, password, workspace));
		if ( session != null ) return session;		
		try {
			session = getRepository().login(new SimpleCredentials(user, password.toCharArray()), workspace);
			if ( session != null ) {
				getSessionMap().put(makeSessionMapKey(user, password, workspace), session);
			}
			return session;
		} catch ( Exception e ) {
			throw new RepositoryException("Impossible to login ", e);
		}
	}
	
	//
	
	private static RawStuff RAW_STUFF = null;
	
	public static void setRawStuff(final RawStuff rawStuff) {
		RAW_STUFF = rawStuff;
	}
	
	//

	private static Map<String, Session> getSessionMap() {
		Map<String, Session> sessionMap = TL_SESSION_MAP.get();
		if ( sessionMap == null ) {
			sessionMap = new HashMap<String, Session>();
			TL_SESSION_MAP.set(sessionMap);
		}
		return sessionMap;
	}	

	private static String makeSessionMapKey(final String userId, final String password, final String workspace) {
		return new StringBuffer().append(userId).append(":").append(password).append(":").append(workspace == null ? "" : workspace).toString();
	}
	
	public interface RawStuff {
		Manager getManager();
	}
		
}
