import sys
from java.lang import System

print "@@@ Starting the script ..."

loadProperties('example.properties')

connect(usr, password, url)
edit()
startEdit()

servermb1=getMBean('Servers/'+str(wlsname1))
servermb2=getMBean('Servers/'+str(wlsname2))
if servermb1 is None or servermb2 is None:
	print '@@@ No server MBean found'

else:

	myWTCServer1 = cmo.createWTCServer("WTCdemo"+str(wlsname1))
	cd("/WTCServers/WTCdemo"+str(wlsname1))

	myLAP1 = create("myLocalTuxDom", "WTCLocalTuxDom")
	myLAP1.setAccessPoint("LocalPoint")
	myLAP1.setAccessPointId(str(wlsname1))
	myLAP1.setNWAddr(wlsNWAddr1)
	myLAP1.setConnectionPolicy("ON_STARTUP")
	myLAP1.setSecurity("NONE")
	print "@@@ WTC local access point created."+str(wlsname1)

	myRAP1 = create("myRTuxDom", "WTCRemoteTuxDom")
	myRAP1.setAccessPoint("RemotePoint")
	myRAP1.setAccessPointId("TDOM1")
	myRAP1.setNWAddr(tuxNWAddr)
	myRAP1.setLocalAccessPoint("LocalPoint")
	print "@@@ WTC remote access point created"+str(wlsname1)

	myER1  = create("myExportedResources", "WTCExport")
	myER1.setResourceName("Mail")
	myER1.setEJBName("MailBean#weblogic.wtc.jatmi.TuxedoServiceHome")
	myER1.setLocalAccessPoint("LocalPoint")
	print "@@@ WTC exported resources created"+str(wlsname1)

	myWTCServer1.addTarget(servermb1)

	cd("/")
	myWTCServer2 = cmo.createWTCServer("WTCdemo"+str(wlsname2))
	cd("/WTCServers/WTCdemo"+str(wlsname2))

	myLAP2 = create("myLocalTuxDom", "WTCLocalTuxDom")
	myLAP2.setAccessPoint("LocalPoint")
	myLAP2.setAccessPointId(str(wlsname2))
	myLAP2.setNWAddr(wlsNWAddr2)
	myLAP2.setConnectionPolicy("ON_STARTUP")
	myLAP2.setSecurity("NONE")
	print "@@@ WTC local access point created."+str(wlsname2)

	myRAP2 = create("myRTuxDom", "WTCRemoteTuxDom")
	myRAP2.setAccessPoint("RemotePoint")
	myRAP2.setAccessPointId("TDOM1")
	myRAP2.setNWAddr(tuxNWAddr)
	myRAP2.setLocalAccessPoint("LocalPoint")
	print "@@@ WTC remote access point created"+str(wlsname2)

	myER2  = create("myExportedResources", "WTCExport")
	myER2.setResourceName("Mail")
	myER2.setEJBName("MailBean#weblogic.wtc.jatmi.TuxedoServiceHome")
	myER2.setLocalAccessPoint("LocalPoint")
	print "@@@ WTC exported resources created"+str(wlsname2)

	myWTCServer2.addTarget(servermb2)

try:
	save()
	activate(block="true")
	print "@@@ WTC Server Resources created."
except:
	print "@@@ Server Resources already exist or there was an error while trying to create new resources."
	dumpstack()

