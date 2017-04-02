# demowls

A demo os using Scala with Weblogic to create an EJB for a Tuxedo call and a webservice. Both of these result in some JMS to eventually send a request to Google Gmail API.

Demo uses Weblogic 12.2


** to install weblogic plugin (I have Weblogic installed in /software/Oracle/Middleware/Oracle_Home)

cd /software/Oracle/Middleware/Oracle_Home/oracle_common/plugins/maven/com/oracle/maven/oracle-maven-sync/12.2.1

mvn install:install-file -DpomFile=oracle-maven-sync-12.2.1.pom  -Dfile=oracle-maven-sync-12.2.1.jar

mvn com.oracle.maven:oracle-maven-sync:push -Doracle-maven-sync.oracleHome=/software/Oracle/Middleware/Oracle_Home

and verify it

mvn help:describe -DgroupId=com.oracle.weblogic -DartifactId=weblogic-maven-plugin -Dversion=12.2.1-2-0

------------------------------------------------------

things to change  (path from /src/weblogic to where ever you cloned to)
example.properties    change the paths
UBB.src               change all entries containing jasonb-Aspire-V3-771 to your hostname as well as path
tuxedo/env.sh         change the path 

Make sure JAVA_HOME is set to JDK18

------------------------------------------------------

** to create weblogic domain

export JAVA_OPTIONS="-Djava.security.egd=file:/dev/./urandom -DUseSunHttpHandler=true"

Need JAVA_OPTIONS for Gmail api and to make sure weblogic starts quickly on linux.

then 

mvn com.oracle.weblogic:weblogic-maven-plugin:create-domain -DdomainHome=/src/weblogic/demowls/maven-domain \
-DdomainTemplate=/src/weblogic/demowls/cluster.jar -DmiddlewareHome=/software/Oracle/Middleware/Oracle_Home \
-Duser=weblogic -Dpassword=123dummy

This uses the cluster.jar as a template for weblogic plugin to make the domain

You can change the admin port using
```
	cd /src/weblogic/demowls/maven-domain/
	. ./env.sh
	java weblogic.WLST ChangeAdminPort.py
```

just be warned that the script bin/startManagedWebLogic.sh wont be updated just config.xml


Start domain by
```
cd /src/weblogic/demowls/maven-domain/
mkdir -p servers/BJManagedServer1/security
mkdir -p servers/BJManagedServer2/security
cp  ./servers/BJAdminServer/security/boot.properties servers/BJManagedServer1/security
cp  ./servers/BJAdminServer/security/boot.properties servers/BJManagedServer2/security
```

```
 ./startWebLogic.sh &
bin/startManagedWebLogic.sh BJManagedServer1 &
bin/startManagedWebLogic.sh BJManagedServer2 &
```

```
cd /src/weblogic/demowls
```
create Tuxedo user
```
	. ./maven-domain/env.sh
	java weblogic.WLST CreateUser.py
```
create Tuxedo domains
```
	. ./maven-domain/env.sh
	java weblogic.WLST CreateWTCResources.py
```

create JMS Server
```
	. ./maven-domain/env.sh
	java weblogic.WLST JMSServer.py
```

create JMS module and queues
```
	. ./maven-domain/env.sh
	java weblogic.WLST JMSModule.py
```


## Tuxedo

the tuxedo bit
```
cd tuxedo
```

update env.sh
```
. ./env.sh
cd config
tmloadcf -y UBB.src
dmloadcf -y BDM.src
tmboot -y
edit ud.in and change email address 
ud32 -i ud.in
```

---------------------------------------------------------------

```
./mvnw package
```

may need install so myeclipse works
```
./mvnw install
./mvnw eclipse:myeclipse
```

to deply to weblogic server
```
./mvnw integration-test
```
