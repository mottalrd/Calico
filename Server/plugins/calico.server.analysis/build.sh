#!/bin/bash

#==================================================
# Compile the plugin and update it in Calico core
# Usage: $(updateAnalysisPluginDistInCalico CALICO_SERVER_HOME)
#==================================================
function updateAnalysisPluginDistInCalico () {

	CALICO_SERVER_HOME=$1
	
	#Compile the plugin
	cd $CALICO_SERVER_HOME/plugins/calico.server.analysis/
	ant clean && ant dist 
	
	#Move to client core
	cd $CALICO_SERVER_HOME
	cp plugins/calico.server.analysis/dist/calico.server.analysis-trunk/calico.server.analysis.jar core/dist/calicoserver-trunk/plugins/calico.server.analysis.jar
	cp plugins/calico.server.analysis/dist/calico.server.analysis-trunk/calico.server.analysis.jar core/plugins/calico.server.analysis.jar

}

#==================================================
# Compile the plugin and update it in Calico core
# Usage: $(updateCalicoDistInAnalysisPlugin CALICO_SERVER_HOME)
#==================================================
function updateCalicoDistInAnalysisPlugin () {

	CALICO_SERVER_HOME=$1
	
	#Compile the core
	cd $CALICO_SERVER_HOME/core
	ant clean && ant dist 
	
	#Move core to plugin
	cd $CALICO_SERVER_HOME
	cp core/dist/calicoserver-trunk/calicoserver.jar plugins/calico.server.analysis/lib/calicoserver.jar
	cp core/dist/calicoserver-trunk/calicoserver.jar plugins/calico.server.analysis/dist/calico.server.analysis-trunk/libs/calicoserver.jar

}

#==================================================
# Reads property $2 from properties file $1 
# and echos the value. Usage:
# V=$(getProp filename property)
#==================================================
function getProp () {
	# ignore lines with '#' as the first non-space character (comments)
    # grep for the property we want
    # get the last match just in case
    # strip the "property=" part but leave any '=' characters in the value

    echo `sed '/^[[:space:]]*\#/d' $1 | grep $2  | tail -n 1 | cut -d "=" -f2- | sed 's/^[[:space:]]*//;s/[[:space:]]*$//'`
}

#==================================================
# MAIN RUN
#==================================================

CALICO_SERVER_HOME=$(getProp conf/calico.analysis.properties CALICO_SERVER_HOME)

updateCalicoDistInAnalysisPlugin $CALICO_SERVER_HOME
updateAnalysisPluginDistInCalico $CALICO_SERVER_HOME