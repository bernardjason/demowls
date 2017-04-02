"""
This script add user, TDOM1, to WebLogic Server User list in the security
realm.
"""

import sys
from java.lang import System

print "@@@ Starting the add user script ..."

loadProperties('example.properties')

connect(usr, password, url)
cd('/')
users={'TDOM1':'tdom1pass'}
groupname='Administrators'
cd('SecurityConfiguration/'+domainName)
cd('Realms/myrealm/AuthenticationProviders/myrealmDefaultAuthenticator')
try:
  for username, userpwd in users.items():
    cmo.createUser(username, userpwd, '')
    print 'creating managed server ' + username
    cmo.addMemberToGroup(groupname, username)
  disconnect()
except:
  print "@@@ User TDOM1 already exist."
  disconnect()
stopEdit()

