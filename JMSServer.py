import sys
from java.lang import System

print "@@@ Starting the script ..."

loadProperties('example.properties')

connect(usr, password, url)

cd('/')
edit()
startEdit()
cmo.createJMSServer('JMSServer')

cd('/JMSServers/JMSServer')
set('Targets',jarray.array([ObjectName('com.bea:Name=Cluster,Type=Cluster')], ObjectName))

activate()
