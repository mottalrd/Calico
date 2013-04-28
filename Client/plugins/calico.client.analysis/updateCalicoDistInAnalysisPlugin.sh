#!/bin/bash
CALICO_CLIENT_HOME="/home/motta/workspace/irvine/Calico-Analysis/Client"

cd $CALICO_CLIENT_HOME/core
ant clean && ant dist 

cd $CALICO_CLIENT_HOME
cp core/dist/calicoclient-trunk/calicoclient.jar plugins/calico.client.analysis/lib/calicoclient.jar
