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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jcr.Session;

import org.apache.jackrabbit.core.nodetype.InvalidNodeTypeDefException;
import org.apache.jackrabbit.ocm.exception.RepositoryException;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.AnnotationMapperImpl;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import play.Play;

import com.wingnest.play2.jackrabbit.plugin.JackrabbitLogger;
import com.wingnest.play2.jackrabbit.plugin.utils.TypeUtils;

final public class OCM {

	/** namespace prefix constant */
	public static final String OCM_NAMESPACE_PREFIX = "ocm";

	/** namespace constant */
	public static final String OCM_NAMESPACE = "http://jackrabbit.apache.org/ocm";

	private static final String MODELS_PACKAGE = "models";
	
	public static ObjectContentManager getOCM()  {
		try {
			final Session session = getLocalSession(Jcr.getConfig().getUserId(), Jcr.getConfig().getPassword());			
			return createOCM(session);
		} catch ( RepositoryException e ) {
			throw e;
		} catch ( Exception e ) {
			throw new RepositoryException(e);
		}
	}
	
	public static ObjectContentManager getOCM(final String workspace)  {
		try {
			final Session session = getLocalSession(Jcr.getConfig().getUserId(), Jcr.getConfig().getPassword(), workspace);			
			return createOCM(session);
		} catch ( RepositoryException e ) {
			throw e;
		} catch ( Exception e ) {
			throw new RepositoryException(e);
		}
	}

	public static ObjectContentManager getOCM(final String userId, final String password) {
		try {
			final Session session = getLocalSession(userId, password);			
			return createOCM(session);
		} catch ( RepositoryException e ) {
			throw e;
		} catch ( Exception e ) {
			throw new RepositoryException(e);
		}
	}
	
	public static ObjectContentManager getOCM(final String userId, final String password, final String workspace) {
		try {
			final Session session = getLocalSession(userId, password, workspace);			
			return createOCM(session);
		} catch ( RepositoryException e ) {
			throw e;
		} catch ( Exception e ) {
			throw new RepositoryException(e);
		}
	}	

	//

	final private static class Nodes {
		private final static Set<Class<?>> nodes;
		static {
			nodes = new HashSet<Class<?>>();
			if ( !Play.isDev() )
				refresh();
		}
		private static void refresh() {
			JackrabbitLogger.debug("Nodes.refresh");
			for ( final  Class<?> javaClass : TypeUtils.getSubTypesOf(Play.application(), MODELS_PACKAGE, null) ) {
				if ( javaClass.isAnnotationPresent(Node.class) ) {
					nodes.add(javaClass);
					JackrabbitLogger.debug("registered Node class : %s ", javaClass.getName());
				}
			}
		}
	}

	private static ObjectContentManager createOCM(final Session session) throws javax.jcr.RepositoryException, InvalidNodeTypeDefException, IOException {
		registerOCMNamespace(session);
		registerNodeTypes(session);
		@SuppressWarnings("rawtypes")
		final List<Class> classes = new ArrayList<Class>();
		if ( Play.isDev() ) {
			Nodes.nodes.clear();
			Nodes.refresh();
		}
		classes.addAll(Nodes.nodes);
		return new ObjectContentManagerImpl(session, new AnnotationMapperImpl(classes));
	}

	private static void registerOCMNamespace(final Session session) throws javax.jcr.RepositoryException {
		JackrabbitLogger.debug("Register namespace");
		final String[] jcrNamespaces = session.getWorkspace().getNamespaceRegistry().getPrefixes();
		boolean createNamespace = true;		
		for ( int i = 0; i < jcrNamespaces.length; i++ ) {
			if ( jcrNamespaces[i].equals(OCM_NAMESPACE_PREFIX) ) {
				createNamespace = false;
				JackrabbitLogger.debug("Jackrabbit OCM namespace exists.");
			}
		}
		if ( createNamespace ) {
			session.getWorkspace().getNamespaceRegistry().registerNamespace(OCM_NAMESPACE_PREFIX, OCM_NAMESPACE);
			JackrabbitLogger.debug("Successfully created Jackrabbit OCM namespace.");
		}
		if ( session.getRootNode() == null ) {
			throw new RepositoryException("Jcr session setup not successful.");
		}
	}

	private static void registerNodeTypes(Session session) throws InvalidNodeTypeDefException, javax.jcr.RepositoryException, IOException {
		// TODO
		return;
		/*
		 * final InputStream xml = new
		 * FileInputStream("./src/test/test-config/nodetypes/custom_nodetypes.xml"
		 * );
		 * 
		 * // HINT: throws InvalidNodeTypeDefException, IOException final
		 * QNodeTypeDefinition[] types = NodeTypeReader.read(xml);
		 * 
		 * final Workspace workspace = session.getWorkspace(); final
		 * NodeTypeManager ntMgr = workspace.getNodeTypeManager(); final
		 * NodeTypeRegistry ntReg = ((NodeTypeManagerImpl)
		 * ntMgr).getNodeTypeRegistry();
		 * 
		 * for ( final int j = 0; j < types.length; j++ ) { final
		 * QNodeTypeDefinition def = types[j]; try {
		 * ntReg.getNodeTypeDef(def.getName()); } catch (
		 * NoSuchNodeTypeException nsne ) { // HINT: if not already registered
		 * than register custom node // type ntReg.registerNodeType(def); }
		 * 
		 * }
		 */
	}
	
	private static Session getLocalSession(final String userId, final String password) throws javax.jcr.RepositoryException {
		final Session session = Jcr.login(userId, password);
		return session;
	}
	
	private static Session getLocalSession(final String userId, final String password, final String workspace) throws javax.jcr.RepositoryException {
		final Session session = Jcr.login(userId, password, workspace);
		return session;
	}		
}
