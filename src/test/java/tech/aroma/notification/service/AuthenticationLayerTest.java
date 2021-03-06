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

import org.apache.thrift.TException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import tech.aroma.thrift.authentication.AuthenticationToken;
import tech.aroma.thrift.authentication.service.AuthenticationService;
import tech.aroma.thrift.authentication.service.VerifyTokenRequest;
import tech.aroma.thrift.exceptions.InvalidTokenException;
import tech.aroma.thrift.exceptions.OperationFailedException;
import tech.aroma.thrift.notification.service.*;
import tech.sirwellington.alchemy.test.junit.runners.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one;
import static tech.sirwellington.alchemy.generator.NumberGenerators.doubles;
import static tech.sirwellington.alchemy.test.junit.ThrowableAssertion.*;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateString.Type.HEXADECIMAL;

/**
 * @author SirWellington
 */
@Repeat(100)
@RunWith(AlchemyTestRunner.class)
public class AuthenticationLayerTest
{

    @Mock
    private AuthenticationService.Iface authenticationService;

    @Mock
    private NotificationService.Iface delegate;

    private SendNotificationRequest sendNotificationRequest;

    @GeneratePojo
    private SendNotificationResponse sendNotificationResponse;

    @GenerateString(HEXADECIMAL)
    private String tokenId;

    @GeneratePojo
    private AuthenticationToken token;

    private AuthenticationLayer instance;

    @Before
    public void setUp() throws TException
    {
        instance = new AuthenticationLayer(authenticationService, delegate);

        verifyZeroInteractions(authenticationService, delegate);

        setupData();
        setupMocks();

    }

    @DontRepeat
    @Test
    public void testConstructor() throws Exception
    {
        assertThrows(() -> new AuthenticationLayer(null, delegate))
                .isInstanceOf(IllegalArgumentException.class);

        assertThrows(() -> new AuthenticationLayer(authenticationService, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testGetApiVersion() throws Exception
    {
        double expected = one(doubles(1.0, 10.0));
        when(delegate.getApiVersion()).thenReturn(expected);

        double result = instance.getApiVersion();
        assertThat(result, is(expected));
        verify(delegate).getApiVersion();
    }

    @Test
    public void testSendNotification() throws Exception
    {
        SendNotificationResponse response = instance.sendNotification(sendNotificationRequest);
        assertThat(response, is(sendNotificationResponse));
        verify(delegate).sendNotification(sendNotificationRequest);
    }

    @Test
    public void testSendNotificationWithInvalidToken() throws Exception
    {
        VerifyTokenRequest request = new VerifyTokenRequest()
                .setTokenId(tokenId);

        when(authenticationService.verifyToken(request))
                .thenThrow(new InvalidTokenException());

        assertThrows(() -> instance.sendNotification(sendNotificationRequest))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    public void testSendNotificationWhenDelegateThrows() throws Exception
    {
        when(delegate.sendNotification(sendNotificationRequest))
                .thenThrow(new OperationFailedException());

        assertThrows(() -> instance.sendNotification(sendNotificationRequest))
                .isInstanceOf(OperationFailedException.class);
    }

    private void setupData()
    {
        token.setTokenId(tokenId);
        token.unsetOwnerId();

        sendNotificationRequest = new SendNotificationRequest()
                .setToken(token);

    }

    private void setupMocks() throws TException
    {
        when(delegate.sendNotification(sendNotificationRequest))
                .thenReturn(sendNotificationResponse);
    }

}
