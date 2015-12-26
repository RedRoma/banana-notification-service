/*
 * Copyright 2015 Aroma Tech.
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

 
package tech.aroma.banana.notification.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.aroma.banana.thrift.channels.BananaChannel;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;

import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;

/**
 *
 * @author SirWellington
 */
@Internal
@NonInstantiable
public final class NotificationAssertions 
{
    private final static Logger LOG = LoggerFactory.getLogger(NotificationAssertions.class);

    private NotificationAssertions() throws IllegalAccessException
    {
        throw new IllegalAccessException("cannot instantiate");
    }
    
    public static AlchemyAssertion<BananaChannel> validBananaChannel()
    {
        return channel ->
        {
            checkThat(channel)
                .usingMessage("channel is null")
                .is(notNull());
            
            if (!isSet(channel))
            {
                throw new FailedAssertionException("BananaChannel has no value: " + channel);
            }
            
        };
    }
    
    private static boolean isSet(BananaChannel channel)
    {
        if (channel.isSetCustomChannel())
        {
            return true;
        }

        if (channel.isSetEmail())
        {
            return true;
        }

        if (channel.isSetSlackChannel())
        {
            return true;
        }
        
        if(channel.isSetSlackUsername())
        {
            return true;
        }
        
        return false;
    }

}
