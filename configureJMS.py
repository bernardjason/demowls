#############################################################################
#
# @author Copyright (c) 2010 - 2011 by Middleware Magic, All Rights Reserved.
#
#############################################################################
 
from java.io import FileInputStream
import java.lang
import os
import string
 
propInputStream = FileInputStream("example.properties")
configProps = Properties()
configProps.load(propInputStream)
 
# 1 - Connecting details
serverUrl = configProps.get("url")
Username = configProps.get("usr")
Password = configProps.get("password")
 
# 2 - JMSServer details
jmsServerName = configProps.get("jms.server.name")
storeName = configProps.get("store.name")
tragatedJMSServerName = configProps.get("tragated.jms.server.name")
 
# 3 - SystemModule Details
systemModuleName = configProps.get("system.module.name")
tragatedSystemModuleName = configProps.get("tragated.system.module.name")
 
# 4 - ConnectionFactory Details
connectionFactoryName = configProps.get("connection.factory.name")
ConnectionFactoryJNDIName = configProps.get("connection.factory.jndi.name")
 
# 5 - Unit Of Order Details
unitOfOrderValue = configProps.get("unit.of.order.value")
 
# 6 - SubDeployment & Queue Details
queueSubDeploymentName = configProps.get("queue.sub.deployment.name")
queueName = configProps.get("queue.name")
queueJNDIName = configProps.get("queue.jndi.name")
 
#7 - Weblogic Bridge Details:
weblogicDestinationBridge = configProps.get("weblogic.destination.bridge")
weblogicAdapterJNDINoTX = configProps.get("weblogic.adapter.jndi.name")

#8 - Widlfly bridge details:
wildflyDestinationBridge = configProps.get("wildfly.destination.bridge")
wildflyServerUrl = configProps.get("wildfly.server.url")
wildflyJMSUsername = configProps.get("wildfly.jms.username")
wildflyJMSUserPassword = configProps.get("wildfly.jms.password")
wildflyInitialContextFactory = configProps.get("wildfly.initial.context.factory")
wildflyJMSConnectionFactory = configProps.get("wildfly.remote.connection.factory.jndi.name")
wildflyJMSQueueJndi = configProps.get("wildfly.jms.destination.jndi.name")




redirect('wlst.log','false')
 
# 1 - Connecting to the Destination 
connect(Username,Password,serverUrl)
 
edit()
 
# 2 - JMSServer details
print "================== JMSSever ==================="
startEdit()
cmo.createJMSServer(jmsServerName)
print "Created a JMSServer !!"
cd('/Deployments/'+jmsServerName)
cmo.setPersistentStore(getMBean('/FileStores/'+storeName))
print "PersistentStore has been set for the JMSServer !!"
set('Targets',jarray.array([ObjectName('com.bea:Name='+tragatedJMSServerName+',Type=Cluster')], ObjectName))
print "Targeted the JMSServer !!"
activate()
print ""
 
# 3 - SystemModule Details
print "================== SystemModule ==================="
startEdit()
cd('/')
cmo.createJMSSystemResource(systemModuleName)
print "Created a SystemModule !!"
cd('/SystemResources/'+systemModuleName)
set('Targets',jarray.array([ObjectName('com.bea:Name='+tragatedSystemModuleName+',Type=Server')], ObjectName))
print "Targeted the SystemModule !!"
activate()
print ""
 
# 4 - ConnectionFactory Details
print "================== ConnectionFactory ==================="
startEdit()
cd('/JMSSystemResources/'+systemModuleName+'/JMSResource/'+systemModuleName)
cmo.createConnectionFactory(connectionFactoryName)
cd('/JMSSystemResources/'+systemModuleName+'/JMSResource/'+systemModuleName+'/ConnectionFactories/'+connectionFactoryName)
cmo.setJNDIName(ConnectionFactoryJNDIName)
print "Created a ConnectionFactory !!"
cd('/JMSSystemResources/'+systemModuleName+'/JMSResource/'+systemModuleName+'/ConnectionFactories/'+connectionFactoryName+'/SecurityParams/'+connectionFactoryName)
cmo.setAttachJMSXUserId(false)
cd('/JMSSystemResources/'+systemModuleName+'/JMSResource/'+systemModuleName+'/ConnectionFactories/'+connectionFactoryName)
cmo.setDefaultTargetingEnabled(true)
print "Targeted the ConnectionFactory !!"
activate()
print ""
 
# 5 - SubDeployment & Queue Details
print "================== SubDeployment & Queue ==================="
startEdit()
cd('/SystemResources/'+systemModuleName)
cmo.createSubDeployment(queueSubDeploymentName)
print "Created a SubDeployment for Queue !!"
cd('/JMSSystemResources/'+systemModuleName+'/JMSResource/'+systemModuleName)
cmo.createQueue(queueName)
print "Created a Queue !!"
cd('/JMSSystemResources/'+systemModuleName+'/JMSResource/'+systemModuleName+'/Queues/'+queueName)
cmo.setJNDIName(queueJNDIName)
cmo.setSubDeploymentName(queueSubDeploymentName)
cd('/SystemResources/'+systemModuleName+'/SubDeployments/'+queueSubDeploymentName)
set('Targets',jarray.array([ObjectName('com.bea:Name='+jmsServerName+',Type=JMSServer')], ObjectName))
print "Targeted the Queue to the created subdeployment !!"
activate()
print ""

cmd = "rm -f wlst.log"
os.system(cmd)
save()
stopEdit()
