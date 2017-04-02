import sys
from java.lang import System

print "@@@ Starting the script ..."

loadProperties('example.properties')

connect(usr, password, url)

cd('/')
edit()
startEdit()

cmo.createJMSSystemResource('JMSModule')

cd('/JMSSystemResources/JMSModule')
set('Targets',jarray.array([ObjectName('com.bea:Name=Cluster,Type=Cluster')], ObjectName))

activate()

startEdit()
cmo.createSubDeployment('SubDeployment')

cd('/JMSSystemResources/JMSModule/SubDeployments/SubDeployment')
set('Targets',jarray.array([ObjectName('com.bea:Name=Cluster,Type=Cluster')], ObjectName))

activate()

startEdit()

cd('/JMSSystemResources/JMSModule/JMSResource/JMSModule')
cmo.createConnectionFactory('JMSConnectionFactory')

cd('/JMSSystemResources/JMSModule/JMSResource/JMSModule/ConnectionFactories/JMSConnectionFactory')
cmo.setJNDIName('JMSConnectionFactory')

cd('/JMSSystemResources/JMSModule/JMSResource/JMSModule/ConnectionFactories/JMSConnectionFactory/SecurityParams/JMSConnectionFactory')
cmo.setAttachJMSXUserId(false)

cd('/JMSSystemResources/JMSModule/JMSResource/JMSModule/ConnectionFactories/JMSConnectionFactory/ClientParams/JMSConnectionFactory')
cmo.setClientIdPolicy('Restricted')
cmo.setSubscriptionSharingPolicy('Exclusive')
cmo.setMessagesMaximum(10)

cd('/JMSSystemResources/JMSModule/JMSResource/JMSModule/ConnectionFactories/JMSConnectionFactory/TransactionParams/JMSConnectionFactory')
cmo.setXAConnectionFactoryEnabled(true)

cd('/JMSSystemResources/JMSModule/JMSResource/JMSModule/ConnectionFactories/JMSConnectionFactory')
cmo.setDefaultTargetingEnabled(true)

activate()

startEdit()

cd('/JMSSystemResources/JMSModule/JMSResource/JMSModule')
cmo.createUniformDistributedQueue('TheQ')

cd('/JMSSystemResources/JMSModule/JMSResource/JMSModule/UniformDistributedQueues/TheQ')
cmo.setJNDIName('TheQ')

cd('/JMSSystemResources/JMSModule/SubDeployments/SubDeployment')
set('Targets',jarray.array([ObjectName('com.bea:Name=Cluster,Type=Cluster')], ObjectName))

cd('/JMSSystemResources/JMSModule/JMSResource/JMSModule/UniformDistributedQueues/TheQ')
cmo.setSubDeploymentName('SubDeployment')

activate()

