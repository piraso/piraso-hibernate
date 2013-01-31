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

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.piraso.proxy.RegexMethodInterceptorAdapter;
import org.piraso.proxy.RegexMethodInterceptorEvent;
import org.piraso.proxy.RegexProxyFactory;
import org.piraso.server.GroupChainId;

/**
 * {@link SessionFactory} hibernate proxy context logger.
 */
public class SessionFactoryProxyFactory extends AbstractHibernateProxyFactory<SessionFactory> {
    public SessionFactoryProxyFactory(GroupChainId id) {
        super(id, new RegexProxyFactory<SessionFactory>(SessionFactory.class));

        factory.addMethodListener("openSession|getCurrentSession", new GetOrOpenSessionListener());
    }

    private class GetOrOpenSessionListener extends RegexMethodInterceptorAdapter<SessionFactory> {
        @Override
        public void afterCall(RegexMethodInterceptorEvent<SessionFactory> evt) {
            if(getPref().isSessionEnabled()) {
                Session session = (Session) evt.getReturnedValue();

                GroupChainId newId = id.create("session-", session.hashCode());
                evt.setReturnedValue(new SessionProxyFactory(newId).getProxy(session));
            }
        }
    }
}
