"""
This script start an edit session, delete a WTC Server
"""

import sys
from java.lang import System

print "@@@ Starting the script ..."

loadProperties('example.properties')

connect(usr, password, url)
edit()
startEdit()

cmo.destroyWTCServer(getMBean('/WTCServers/WTCdemo'+str(wlsname1)))
cmo.destroyWTCServer(getMBean('/WTCServers/WTCdemo'+str(wlsname2)))

try:
	save()
	activate(block="true")
	print "@@@ WTC System Resources deleted."
except:
	print "@@@ WTC System Resources does not exist or there was an error while trying to delete old resources."
	dumpStack()

stopEdit()
