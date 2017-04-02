print '@@@ assume in domain directory @@@'
readDomain('.')   
cd ('Server')     
cd('BJAdminServer')
get('ListenPort')
set ('ListenPort',1971)
updateDomain()
stopEdit()
exit()
