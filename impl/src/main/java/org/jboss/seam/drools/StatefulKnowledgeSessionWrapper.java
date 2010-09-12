/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.seam.drools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Map;

import org.drools.KnowledgeBase;
import org.drools.command.Command;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.rule.AgendaEventListener;
import org.drools.event.rule.WorkingMemoryEventListener;
import org.drools.marshalling.Marshaller;
import org.drools.marshalling.MarshallerFactory;
import org.drools.marshalling.ObjectMarshallingStrategy;
import org.drools.marshalling.ObjectMarshallingStrategyAcceptor;
import org.drools.runtime.Calendars;
import org.drools.runtime.Channel;
import org.drools.runtime.Environment;
import org.drools.runtime.ExecutionResults;
import org.drools.runtime.ExitPoint;
import org.drools.runtime.Globals;
import org.drools.runtime.ObjectFilter;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.drools.runtime.process.WorkItemManager;
import org.drools.runtime.rule.Agenda;
import org.drools.runtime.rule.AgendaFilter;
import org.drools.runtime.rule.FactHandle;
import org.drools.runtime.rule.LiveQuery;
import org.drools.runtime.rule.QueryResults;
import org.drools.runtime.rule.ViewChangedEventListener;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;
import org.drools.time.SessionClock;

/**
 * 
 * @author Tihomir Surdilovic
 */
public class StatefulKnowledgeSessionWrapper implements StatefulKnowledgeSession, Externalizable
{
   private StatefulKnowledgeSession ksession;

   public StatefulKnowledgeSessionWrapper(StatefulKnowledgeSession ksession)
   {
      this.ksession = ksession;
   }

   public StatefulKnowledgeSessionWrapper()
   {

   }

   public StatefulKnowledgeSession getKsession()
   {
      return ksession;
   }

   public void setKsession(StatefulKnowledgeSession ksession)
   {
      this.ksession = ksession;
   }

   public void dispose()
   {
      ksession.dispose();
   }

   public int getId()
   {
      return ksession.getId();
   }

   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
   {
      byte[] bytes = new byte[in.available()];
      in.read(bytes);
      ByteArrayInputStream bais = new ByteArrayInputStream( bytes );

      KnowledgeBase kbase = (KnowledgeBase) in.readObject();
      Marshaller marshaller = createSerializableMarshaller( kbase );
      ksession = marshaller.unmarshall(bais);
      bais.close();
   }

   public void writeExternal(ObjectOutput out) throws IOException
   {
      Marshaller marshaller = createSerializableMarshaller( ksession.getKnowledgeBase() );
      
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      marshaller.marshall( bos, ksession );
      final byte[] b = bos.toByteArray();
      bos.close();
      
      out.write(b);
      out.writeObject(ksession.getKnowledgeBase());
   }

   public FactHandle getFactHandle(Object object)
   {
      return ksession.getFactHandle(object);
   }

   public <T extends FactHandle> Collection<T> getFactHandles()
   {
      return ksession.getFactHandles();
   }

   public <T extends FactHandle> Collection<T> getFactHandles(ObjectFilter filter)
   {
      return ksession.getFactHandles(filter);
   }

   public Object getObject(FactHandle factHandle)
   {
      return ksession.getObject(factHandle);
   }

   public Collection<Object> getObjects()
   {
      return ksession.getObjects();
   }

   public Collection<Object> getObjects(ObjectFilter filter)
   {
      return ksession.getObjects(filter);
   }

   public FactHandle insert(Object object)
   {
      return ksession.insert(object);
   }

   public void retract(FactHandle handle)
   {
      ksession.retract(handle);
   }

   public void update(FactHandle handle, Object object)
   {
      ksession.update(handle, object);
   }

   public void abortProcessInstance(long id)
   {
      ksession.abortProcessInstance(id);
   }

   public ProcessInstance getProcessInstance(long id)
   {
      return ksession.getProcessInstance(id);
   }

   public Collection<ProcessInstance> getProcessInstances()
   {
      return ksession.getProcessInstances();
   }

   public WorkItemManager getWorkItemManager()
   {
      return ksession.getWorkItemManager();
   }

   public void signalEvent(String type, Object event)
   {
      ksession.signalEvent(type, event);
   }

   public ProcessInstance startProcess(String processId, Map<String, Object> parameters)
   {
      return ksession.startProcess(processId, parameters);
   }

   public ProcessInstance startProcess(String processId)
   {
      return ksession.startProcess(processId);
   }

   public int fireAllRules()
   {
      return ksession.fireAllRules();
   }

   public int fireAllRules(AgendaFilter agendaFilter)
   {
      return ksession.fireAllRules(agendaFilter);
   }

   public int fireAllRules(int max)
   {
      return ksession.fireAllRules(max);
   }

   public void fireUntilHalt()
   {
      ksession.fireUntilHalt();
   }

   public void fireUntilHalt(AgendaFilter agendaFilter)
   {
      ksession.fireUntilHalt(agendaFilter);
   }

   public Environment getEnvironment()
   {
      return ksession.getEnvironment();
   }

   public Object getGlobal(String identifier)
   {
      return ksession.getGlobal(identifier);
   }

   public Globals getGlobals()
   {
      return ksession.getGlobals();
   }

   public KnowledgeBase getKnowledgeBase()
   {
      return ksession.getKnowledgeBase();
   }

   public <T extends SessionClock> T getSessionClock()
   {
      // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6302954
      return ksession.<T> getSessionClock();
   }

   public void registerExitPoint(String name, ExitPoint exitPoint)
   {
      ksession.registerExitPoint(name, exitPoint);
   }

   public void setGlobal(String identifier, Object object)
   {
      ksession.setGlobal(identifier, object);
   }

   public void unregisterExitPoint(String name)
   {
      ksession.unregisterExitPoint(name);
   }

   public void addEventListener(ProcessEventListener listener)
   {
      ksession.addEventListener(listener);
   }

   public Collection<ProcessEventListener> getProcessEventListeners()
   {
      return ksession.getProcessEventListeners();
   }

   public void removeEventListener(ProcessEventListener listener)
   {
      ksession.removeEventListener(listener);
   }

   @SuppressWarnings("unchecked")
   public ExecutionResults execute(Command command)
   {
      return (ExecutionResults) ksession.execute(command);
   }

   public void addEventListener(AgendaEventListener listener)
   {
      ksession.addEventListener(listener);
   }

   public void addEventListener(WorkingMemoryEventListener listener)
   {
      ksession.addEventListener(listener);
   }

   public Collection<AgendaEventListener> getAgendaEventListeners()
   {
      return ksession.getAgendaEventListeners();
   }

   public Collection<WorkingMemoryEventListener> getWorkingMemoryEventListeners()
   {
      return ksession.getWorkingMemoryEventListeners();
   }

   public void removeEventListener(AgendaEventListener listener)
   {
      ksession.removeEventListener(listener);
   }

   public void removeEventListener(WorkingMemoryEventListener listener)
   {
      ksession.removeEventListener(listener);
   }

   public Agenda getAgenda()
   {
      return ksession.getAgenda();
   }

   public QueryResults getQueryResults(String query, Object[] arguments)
   {
      return ksession.getQueryResults(query, arguments);
   }

   public QueryResults getQueryResults(String query)
   {
      return ksession.getQueryResults(query);
   }

   public WorkingMemoryEntryPoint getWorkingMemoryEntryPoint(String name)
   {
      return ksession.getWorkingMemoryEntryPoint(name);
   }
   
   

   public Calendars getCalendars()
   {
      return ksession.getCalendars();
   }

   public Map<String, Channel> getChannels()
   {
      return ksession.getChannels();
   }

   public void registerChannel(String arg0, Channel arg1)
   {
      ksession.registerChannel(arg0, arg1);
      
   }

   public void unregisterChannel(String arg0)
   {
      ksession.unregisterChannel(arg0);
   }

   public void signalEvent(String arg0, Object arg1, long arg2)
   {
      ksession.signalEvent(arg0, arg1, arg2);
   }

   public LiveQuery openLiveQuery(String arg0, Object[] arg1, ViewChangedEventListener arg2)
   {
      return ksession.openLiveQuery(arg0, arg1, arg2);
   }

   public String getEntryPointId()
   {
      return ksession.getEntryPointId();
   }

   public long getFactCount()
   {
      return ksession.getFactCount();
   }

   public Collection<? extends WorkingMemoryEntryPoint> getWorkingMemoryEntryPoints()
   {
      return ksession.getWorkingMemoryEntryPoints();
   }

   public void halt()
   {
      ksession.halt();
   }

   private Marshaller createSerializableMarshaller(KnowledgeBase knowledgeBase)
   {
      ObjectMarshallingStrategyAcceptor acceptor = MarshallerFactory.newClassFilterAcceptor(new String[] { "*.*" });
      ObjectMarshallingStrategy strategy = MarshallerFactory.newSerializeMarshallingStrategy(acceptor);
      Marshaller marshaller = MarshallerFactory.newMarshaller(knowledgeBase, new ObjectMarshallingStrategy[] { strategy });
      return marshaller;
   }

}
