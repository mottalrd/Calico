#!/bin/bash

#==================================================
# Compile the plugin and update it in Calico core
# Usage: $(updateAnalysisPluginDistInCalico CALICO_CLIENT_HOME)
#==================================================
function updateAnalysisPluginDistInCalico () {

	CALICO_CLIENT_HOME=$1
	
	#Compile the plugin
	cd $CALICO_CLIENT_HOME/plugins/calico.client.analysis/
	ant clean && ant dist 
	
	#Move to client core
	cd $CALICO_CLIENT_HOME
	cp plugins/calico.client.analysis/dist/calico.client.analysis-trunk/calico.client.analysis.jar core/dist/calicoclient-trunk/plugins/calico.client.analysis.jar
	cp plugins/calico.client.analysis/dist/calico.client.analysis-trunk/calico.client.analysis.jar core/plugins/calico.client.analysis.jar
	
	cp plugins/calico.client.analysis/conf/calico.analysis.properties core/dist/calicoclient-trunk/conf/calico.analysis.properties
	cp plugins/calico.client.analysis/conf/calico.analysis.properties core/conf/calico.analysis.properties
}

#==================================================
# Compile the plugin and update it in Calico core
# Usage: $(updateCalicoDistInAnalysisPlugin CALICO_CLIENT_HOME)
#==================================================
function updateCalicoDistInAnalysisPlugin () {

	CALICO_CLIENT_HOME=$1
	
	#Compile the core
	cd $CALICO_CLIENT_HOME/core
	ant clean && ant dist 
	
	#Move to the client plugin
	cd $CALICO_CLIENT_HOME
	cp core/dist/calicoclient-trunk/calicoclient.jar plugins/calico.client.analysis/lib/calicoclient.jar
	cp core/dist/calicoclient-trunk/calicoclient.jar plugins/calico.client.analysis/dist/calico.client.analysis-trunk/libs/calicoclient.jar

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

CALICO_CLIENT_HOME=$(getProp conf/calico.analysis.properties CALICO_CLIENT_HOME)

updateCalicoDistInAnalysisPlugin $CALICO_CLIENT_HOME
updateAnalysisPluginDistInCalico $CALICO_CLIENT_HOME