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


package tech.aroma.notification.service.pigeon;

import org.apache.thrift.TBase;
import tech.aroma.thrift.channels.AromaChannel;
import tech.aroma.thrift.events.Event;
import tech.aroma.thrift.exceptions.OperationFailedException;
import tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern;

import static tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern.Role.INTERFACE;


/**
 * A Pigeon is responsible for sending a Message to a particular {@link AromaChannel}.
 *
 * @param <C> The particular {@link AromaChannel} an implementation delivers.
 * @author SirWellington
 */
@StrategyPattern(role = INTERFACE)
public interface Pigeon<C extends TBase>
{
    /**
     * Delivers a message to a particular {@link AromaChannel}.
     *
     * @param message
     * @param channel
     * @throws OperationFailedException If the message could not be delivered.
     */
    void deliverMessageTo(Event message, C channel) throws OperationFailedException;
}
