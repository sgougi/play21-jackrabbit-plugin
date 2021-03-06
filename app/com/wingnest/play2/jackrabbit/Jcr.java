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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.nodetype.NodeTypeManager;

import org.apache.jackrabbit.core.nodetype.InvalidNodeTypeDefException;
import org.apache.jackrabbit.core.nodetype.NodeTypeManagerImpl;
import org.apache.jackrabbit.core.nodetype.NodeTypeRegistry;
import org.apache.jackrabbit.core.nodetype.xml.NodeTypeReader;
import org.apache.jackrabbit.spi.QNodeTypeDefinition;

import com.wingnest.play2.jackrabbit.plugin.manager.Manager;
import com.wingnest.play2.jackrabbit.plugin.manager.Manager.Config;


final public class Jcr {

	final private static ThreadLocal<Map<String, Session>> TL_SESSION_MAP = new ThreadLocal<Map<String, Session>>(); 
	
	public static Config getConfig() {
		return RAW_STUFF.getManager().getConfig();
	}

	public static Repository getRepository() {
		return RAW_STUFF.getManager().getRepository();
	}

	public static Session getCurrentSession(final String userId, final String workspace) {
		final String key = makeSessionMapKey(userId, workspace);
		final Session session = getSessionMap().get(key);
		if ( session != null && !session.isLive() ) {
			getSessionMap().remove(key);
			return null;
		}		
		return session;
	}
	
	public static Session getCurrentSession(final String userId) {
		return getCurrentSession(userId, null);
	}	
	
	public static void setCurrentSession(final Session session, final String userId) throws RepositoryException   {
		setCurrentSession(session, userId, null); 
	}
	
	public static void setCurrentSession(final Session session, final String userId, final String workspace) throws RepositoryException   {
		final String key = makeSessionMapKey(userId, workspace);
		Session curSession = getSessionMap().get(key);
		if ( curSession != null && session.isLive() ) {
			getSessionMap().remove(key);
			curSession.logout();
		}
		if ( session != null ) {
			try {
				registerNodeTypes(session);
			} catch ( Exception e ) {
				throw new RepositoryException("Impossible to set current session", e);
			}											
			getSessionMap().put(key, session);				
		}
	}
	
	public static Session login(final String userId, final String password) throws RepositoryException  {
		return login(userId, password, null);
	}
	
	public static Session login(final String userId, final String password, final String workspace) throws RepositoryException  {
		final String key = makeSessionMapKey(userId, workspace);
		Session session = getSessionMap().get(key);
		if ( session != null && session.isLive() ) {
			getSessionMap().remove(key);
			session.logout();
		}
		try {
			session = getRepository().login(new SimpleCredentials(userId, password.toCharArray()), workspace);
			if ( session != null ) {
				registerNodeTypes(session);											
				getSessionMap().put(key, session);				
			}
			return session;
		} catch ( Exception e ) {
			throw new RepositoryException("Impossible to login", e);
		}
	}
	
	//

	public interface RawStuff {
		Manager getManager();
	}

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

	private static String makeSessionMapKey(final String userId, final String workspace) {
		return new StringBuffer().append(userId).append(":").append(workspace == null ? "" : workspace).toString();
	}

	private static void registerNodeTypes(final Session session) throws InvalidNodeTypeDefException, javax.jcr.RepositoryException, IOException {
		final String nodeTypesXml = Jcr.getConfig().getNodeTypesXml();
		if ( nodeTypesXml == null )
			return;

		final InputStream xml = new FileInputStream(nodeTypesXml);
		final QNodeTypeDefinition[] types = NodeTypeReader.read(xml);
		final Workspace workspace = session.getWorkspace();
		final NodeTypeManager ntMgr = workspace.getNodeTypeManager();
		final NodeTypeRegistry ntReg = ((NodeTypeManagerImpl) ntMgr).getNodeTypeRegistry();

		for ( int j = 0; j < types.length; j++ ) {
			final QNodeTypeDefinition def = types[j];
			try {
				ntReg.getNodeTypeDef(def.getName());
			} catch ( NoSuchNodeTypeException nsne ) {
				// HINT: if not already registered than register custom node type
				ntReg.registerNodeType(def);
			}
		}
	}	
}
