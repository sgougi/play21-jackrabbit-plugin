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
package com.wingnest.play2.jackrabbit.plugin.director;

import static com.wingnest.play2.jackrabbit.plugin.ConfigConsts.*;

import com.wingnest.play2.jackrabbit.plugin.manager.Manager;
import com.wingnest.play2.jackrabbit.plugin.manager.Manager.Config;

import play.Configuration;
import play.Play;

public class DefaultDirector implements Director {

	private final Manager manager;

	public DefaultDirector(final Manager manager) {
		this.manager = manager;
		final Config config = loadConfigForManager(manager);
		manager.initialize(config);
	}

	@Override
	public void onApplicationStart() {

	}

	@Override
	public Manager getManager() {
		return this.manager;
	}

	//

	protected Config loadConfigForManager(final Manager manager) {
		final Configuration c = Play.application().configuration();
		final String userid = c.getString(CONF_JCR_USERID, "anonymous");
		final String password = c.getString(CONF_JCR_PASSWORD, "");
		final String repositoryUri = c.getString(CONF_JCR_REPOSITORY_URI, "file:./repository");
		// String workspace = p.getProperty(CONF_JCR_DEFAULT_WORKSPACE, "default"); // TODO 不要か？
		final String repoConfiguration = c.getString(CONF_JCR_REPOSITORY_CONFIG, "./conf/repository.xml");
		final String strHasRecreateRequire = c.getString(CONF_JCR_HAS_RECREATION_REQUIRE, "false");
		return new DefaultConfig(userid, password, repoConfiguration, repositoryUri, strHasRecreateRequire);
	}

	private static class DefaultConfig implements Manager.Config {

		private final String userid;
		private final String password;
		private final String repoConfiguration;
		private final String repositoryUri;
		private final boolean hasRecreateRequire;

		public DefaultConfig(final String userid, final String password, final String repoConfiguration, final String repositoryUri, final String strHasRecreateRequire) {
			this.userid = userid;
			this.password = password;
			this.repoConfiguration = repoConfiguration;
			this.repositoryUri = repositoryUri;
			this.hasRecreateRequire = Boolean.parseBoolean(strHasRecreateRequire.trim());
		}

		@Override
		public String getUserId() {
			return userid;
		}

		@Override
		public String getPassword() {
			return password;
		}

		@Override
		public String getRepoConfiguration() {
			return repoConfiguration;
		}

		@Override
		public String getRepositoryUrl() {
			return repositoryUri;
		}

		@Override
		public boolean hasRecreateRequire() {
			return hasRecreateRequire;
		}

	}

}
