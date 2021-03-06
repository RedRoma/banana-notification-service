/*
 * Copyright 2017 RedRoma, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.aroma.notification.service.server;

import java.net.SocketException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.aroma.notification.service.ModuleNotificationService;
import tech.aroma.notification.service.operations.ModuleNotificationOperations;
import tech.aroma.thrift.notification.service.NotificationService;
import tech.aroma.thrift.notification.service.NotificationServiceConstants;
import tech.sirwellington.alchemy.annotations.access.Internal;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * This Main Class runs the Notification Service on a Server Socket.
 *
 * @author SirWellington
 */
@Internal
public final class TcpServer
{

    private final static Logger LOG = LoggerFactory.getLogger(TcpServer.class);
    private static final int PORT = NotificationServiceConstants.SERVICE_PORT;

    public static void main(String[] args) throws TTransportException, SocketException
    {
        Injector injector = Guice.createInjector(new ModuleNotificationService(),
                                                 new ModuleNotificationOperations());

        NotificationService.Iface bananaService = injector.getInstance(NotificationService.Iface.class);
        NotificationService.Processor processor = new NotificationService.Processor<>(bananaService);

        TServerSocket socket = new TServerSocket(PORT);
        socket.getServerSocket().setSoTimeout((int) SECONDS.toMillis(60));

        TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(socket)
                .protocolFactory(new TBinaryProtocol.Factory())
                .processor(processor)
                .requestTimeout(60)
                .requestTimeoutUnit(SECONDS)
                .minWorkerThreads(5)
                .maxWorkerThreads(100);

        LOG.info("Starting Notification Service at port {}", PORT);

        TThreadPoolServer server = new TThreadPoolServer(serverArgs);
        server.serve();
        server.stop();
    }
}
