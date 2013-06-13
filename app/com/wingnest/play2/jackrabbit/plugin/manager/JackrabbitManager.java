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
package com.wingnest.play2.jackrabbit.plugin.manager;

import static org.apache.jackrabbit.core.config.RepositoryConfigurationParser.REPOSITORY_HOME_VARIABLE;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

import javax.jcr.Repository;

import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.BeanConfig;
import org.apache.jackrabbit.core.config.BeanConfigVisitor;
import org.apache.jackrabbit.core.config.ConfigurationException;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.apache.jackrabbit.core.config.RepositoryConfigurationParser;
import org.apache.jackrabbit.ocm.exception.RepositoryException;
import org.apache.jackrabbit.rmi.client.ClientAdapterFactory;
import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;
import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;
import org.xml.sax.InputSource;

import play.Play;

import com.wingnest.play2.jackrabbit.plugin.JackrabbitLogger;
import com.wingnest.play2.jackrabbit.plugin.utils.FileUtils;

public class JackrabbitManager implements Manager {

	private Config config;
	private Repository repository;

	@Override
	public void initialize(final Config config) {
		this.config = config;
		JackrabbitLogger.debug("config = %s", config);
		try {
			repository = createRepository(config.getRepositoryUrl());
		} catch ( RepositoryException e ) {
			throw e;
		} catch ( Exception e ) {
			throw new RepositoryException(e);
		}
	}

	private Repository createRepository(final String url) {
		if ( url.startsWith("rmi://") ) {
			return createRmiRepository(url);
		} else if ( url.startsWith("file:") ) {
			return createFileRepository(url);
		} else if ( url.startsWith("http://") ) {
			return createUrlRemoteRepository(url);
		} else {
			throw new RepositoryException("Unsupported repository location url. : " + url);
		}
	}

	private Repository createFileRepository(final String url) {
		final File repoDir;
		if ( url.startsWith("file://") ) {
			try {
				repoDir = new File(new URL(url).getPath());
			} catch ( MalformedURLException e ) {
				throw new RepositoryException(e);
			}
		} else {
			repoDir = new File(url.substring(5));
		}
		JackrabbitLogger.debug("repoDir : %s", repoDir.getAbsolutePath());
		if ( config.hasRecreateRequire() ) {
			try {
				JackrabbitLogger.debug("delete repoDir : " + repoDir.getAbsolutePath());
				FileUtils.deepDelete(repoDir);
			} catch ( IOException e ) {
				throw new RepositoryException("Could not delete directory: " + repoDir.getAbsolutePath());
			}
		}
		if ( !repoDir.exists() ) {
			if ( !repoDir.mkdirs() ) {
				throw new RepositoryException("Could not create directory: " + repoDir.getAbsolutePath());
			}
		}
		try {
			final RepositoryConfig repoConfig = PlayRepositoryConfig.create(config.getRepoConfiguration(), repoDir.getCanonicalPath());
			return RepositoryImpl.create(repoConfig);
		} catch ( Exception e ) {
			throw new RepositoryException(e);
		}
	}

	private Repository createUrlRemoteRepository(final String url) {
		try {
			return new URLRemoteRepository(url);
		} catch ( Exception e ) {
			throw new RepositoryException("Could not create url remote repository instance at url: " + url, e);
		}
	}
	
	private Repository createRmiRepository(final String url) {
		try {
			Play.application().classloader().loadClass("org.apache.jackrabbit.rmi.client.ClientRepositoryFactory");
			final ClientRepositoryFactory factory = new ClientRepositoryFactory(new ClientAdapterFactory());
			return factory.getRepository(url);
		} catch ( Exception e ) {
			throw new RepositoryException("Could not create rmi repository instance at url: " + url, e);
		}
	}	

	@Override
	public Config getConfig() {
		return config;
	}

	@Override
	public Repository getRepository() {
		return repository;
	}
	
	/**
	 * Replacement class which injects the Play classloader.
	 */
	private static class PlayRepositoryConfig {
	  
		/*
		 * Static methods can't be overridden in Java, so this is a condensed
		 * version of RepositoryConfig.
		 */
		public static RepositoryConfig create(String file, String home)
				throws ConfigurationException {
			URI uri = new File(file).toURI();
			Properties variables = new Properties(System.getProperties());
			variables.setProperty(REPOSITORY_HOME_VARIABLE, home);
			return create(new InputSource(uri.toString()), variables);
		}
		
		/*
		 * This is a direct copy from RepositoryConfig, except where marked.
		 */		
		public static RepositoryConfig create(InputSource xml, Properties variables)
				throws ConfigurationException {
			RepositoryConfigurationParser parser =
				new RepositoryConfigurationParser(variables);
			
			// Attach visitor to set Play classloader on all configs
			attachVisitor(parser);
				
			RepositoryConfig config = parser.parseRepositoryConfig(xml);
			config.init();
	
			return config;
		}
		
		private static void attachVisitor(RepositoryConfigurationParser parser) {
			BeanConfigVisitor visitor = new BeanConfigVisitor() {
				public void visit(BeanConfig config) {
					ClassLoader cl = Play.application().classloader();
					config.setDefaultClassLoader(cl);
					config.setClassLoader(cl);
				}
			};
			parser.setConfigVisitor(visitor);
		}
		
	}

}
