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

package tech.aroma.notification.service;

import javax.inject.Inject;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.aroma.thrift.AromaConstants;
import tech.aroma.thrift.exceptions.*;
import tech.aroma.thrift.notification.service.*;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.designs.patterns.DecoratorPattern;
import tech.sirwellington.alchemy.thrift.operations.ThriftOperation;

import static tech.aroma.thrift.assertions.AromaAssertions.checkNotNull;
import static tech.sirwellington.alchemy.annotations.designs.patterns.DecoratorPattern.Role.CONCRETE_COMPONENT;
import static tech.sirwellington.alchemy.arguments.Arguments.*;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;

/**
 * @author SirWellington
 */
@Internal
@DecoratorPattern(role = CONCRETE_COMPONENT)
final class NotificationServiceBase implements NotificationService.Iface
{

    private final static Logger LOG = LoggerFactory.getLogger(NotificationServiceBase.class);

    private final ThriftOperation<SendNotificationRequest, SendNotificationResponse> sendNotificationOperation;

    @Inject
    NotificationServiceBase(ThriftOperation<SendNotificationRequest, SendNotificationResponse> sendNotificationOperation)
    {
        checkThat(sendNotificationOperation)
                .are(notNull());

        this.sendNotificationOperation = sendNotificationOperation;
    }

    @Override
    public double getApiVersion() throws TException
    {
        return AromaConstants.API_VERSION;
    }

    @Override
    public SendNotificationResponse sendNotification(SendNotificationRequest request) throws InvalidArgumentException,
                                                                                             OperationFailedException,
                                                                                             InvalidTokenException,
                                                                                             TException
    {
        checkNotNull(request, "request is missing");

        LOG.info("Received request to send notification: {}", request);

        return sendNotificationOperation.process(request);
    }

}
