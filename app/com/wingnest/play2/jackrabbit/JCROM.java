package com.wingnest.play2.jackrabbit;

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
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.core.nodetype.InvalidNodeTypeDefException;
import org.jcrom.Jcrom;
import org.jcrom.SessionFactory;
import org.jcrom.annotations.JcrNode;

import play.Play;

import com.wingnest.play2.jackrabbit.plugin.ConfigConsts;
import com.wingnest.play2.jackrabbit.plugin.JackrabbitLogger;
import com.wingnest.play2.jackrabbit.plugin.exceptions.JackrabbitUnexpectedException;
import com.wingnest.play2.jackrabbit.plugin.utils.TypeUtils;

public class JCROM {

	public static Jcrom getJcrom(final Session session)  {
		try {
			return createJcrom(session);
		} catch ( Exception e ) {
			throw new JackrabbitUnexpectedException(e);
		}
	}
	
	public static Jcrom getJcrom()  {
		try {
			final String userId = Jcr.getConfig().getUserId();
			Session session = Jcr.getCurrentSession(userId);
			if ( session == null )
				session = getLocalSession(userId, Jcr.getConfig().getPassword());			
			return createJcrom(session);
		} catch ( Exception e ) {
			throw new JackrabbitUnexpectedException(e);
		}
	}
	
	public static Jcrom getJcrom(final String workspace)  {
		try {
			final String userId = Jcr.getConfig().getUserId();
			Session session = Jcr.getCurrentSession(userId, workspace);
			if ( session == null )
				session = getLocalSession(Jcr.getConfig().getUserId(), Jcr.getConfig().getPassword(), workspace);			
			return createJcrom(session);
		} catch ( Exception e ) {
			throw new JackrabbitUnexpectedException(e);
		}
	}

	public static Jcrom getJcrom(final String userId, final String password) {
		try {
			Session session = Jcr.getCurrentSession(userId);
			if ( session == null )
				session = getLocalSession(userId, password);			
			return createJcrom(session);
		} catch ( Exception e ) {
			throw new JackrabbitUnexpectedException(e);
		}
	}
	
	public static Jcrom getJcrom(final String userId, final String password, final String workspace) {
		try {
			Session session = Jcr.getCurrentSession(userId, workspace);
			if ( session == null )
				session = getLocalSession(userId, password, workspace);			
			return createJcrom(session);
		} catch ( Exception e ) {
			throw new JackrabbitUnexpectedException(e);
		}
	}
	
	final private static class Nodes {
		private final static Set<Class<?>> nodes;
		static {
			nodes = new HashSet<Class<?>>();
			if ( !Play.isDev() )
				refresh();
		}
		private static void refresh() {
			JackrabbitLogger.debug("Nodes.refresh");
			//org.jcrom.util.ReflectionUtils.getClasses(Play.application().classloader(), ConfigConsts.MODELS_PACKAGE);
			for ( final  Class<?> javaClass : TypeUtils.getSubTypesOf(Play.application(), ConfigConsts.MODELS_PACKAGE, null) ) {
				if ( javaClass.isAnnotationPresent(JcrNode.class) ) {
					nodes.add(javaClass);
					JackrabbitLogger.debug("registered Node class : %s ", javaClass.getName());
				}
			}
		}
	}

	private static Jcrom createJcrom(final Session session) throws javax.jcr.RepositoryException, InvalidNodeTypeDefException, IOException {
		final Set<Class<?>> classes = new HashSet<Class<?>>();
		if ( Play.isDev() ) {
			Nodes.nodes.clear();
			Nodes.refresh();
		}
		classes.addAll(Nodes.nodes);
		final Jcrom jcrom = new Jcrom(classes);
		jcrom.setSessionFactory(new SessionFactory(){
			@Override
			public Session getSession() throws RepositoryException {
				return session;
			}});
		return jcrom;
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
