/**
 * Copyright 2022 Jordan Sucur, HSLU Informatik, Switzerland
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.hslu.informatik.swde.wda.rws.server;

import ch.hslu.informatik.swde.wda.rws.resources.WdaResource;
import ch.hslu.informatik.swde.wda.rws.util.LocalDateTimeConverterProvider;
import ch.hslu.informatik.swde.wda.rws.util.ServerChecker;
import com.sun.net.httpserver.HttpServer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
public class ServerApp {

	private static String URI_BASE = "http://localhost:8080/";

	public static void main(String[] args) {

		URI uri = URI.create(URI_BASE);
		ResourceConfig resConf = new ResourceConfig(WdaResource.class);

		resConf.register(LocalDateTimeConverterProvider.class);

		ServerChecker.checkServer("eee-03317.simple.eee.intern", 8080);
		ServerChecker.checkServer("localhost", 5432);

		HttpServer srv = JdkHttpServerFactory.createHttpServer(uri, resConf);

		System.out.println("Server running at " + URI_BASE);
		System.out.println("Press ENTER to shut down ...");

		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		srv.stop(1);
		System.out.println("Execution stopped ...");
	}
}