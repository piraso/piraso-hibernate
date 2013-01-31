/*
 * Copyright (c) 2012. Piraso Alvin R. de Leon. All Rights Reserved.
 *
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The Piraso licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.piraso.server.hibernate.logger;

import org.hibernate.classic.Session;
import org.piraso.api.Level;
import org.piraso.api.hibernate.HibernatePreferenceEnum;
import org.piraso.proxy.RegexProxyFactory;
import org.piraso.server.GroupChainId;
import org.piraso.server.logger.MethodCallLoggerListener;
import org.piraso.server.logger.SimpleMethodLoggerListener;

/**
 * {@link org.hibernate.Session} hibernate proxy context logger.
 */
public class SessionProxyFactory extends AbstractHibernateProxyFactory<Session> {

    private static final Level METHOD_CALL_LEVEL = Level.get(HibernatePreferenceEnum.SESSION_METHOD_CALL_ENABLED.getPropertyName());

    private static final Level BASE_LEVEL = Level.get(HibernatePreferenceEnum.SESSION_ENABLED.getPropertyName());

    public SessionProxyFactory(GroupChainId id) {
        super(id, new RegexProxyFactory<Session>(Session.class));

        if(getPref().isSessionMethodCallEnabled()) {
            factory.addMethodListener(".*", new MethodCallLoggerListener<Session>(METHOD_CALL_LEVEL, id));
        }

        if(getPref().isSessionEnabled()) {
            if(!getPref().isSessionMethodCallEnabled()) {
                factory.addMethodListener("flush|clear|close", new SimpleMethodLoggerListener<Session>(BASE_LEVEL, id));
            }
        }
    }
}
