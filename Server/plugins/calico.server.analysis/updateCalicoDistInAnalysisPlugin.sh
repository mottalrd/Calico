#!/bin/bash
CALICO_SERVER_HOME="/home/motta/workspace/irvine/Calico-Analysis/Server"

cd $CALICO_SERVER_HOME/core
ant clean && ant dist 

cd $CALICO_SERVER_HOME
cp core/dist/calicoserver-trunk/calicoserver.jar plugins/calico.server.analysis/lib/calicoserver.jar
