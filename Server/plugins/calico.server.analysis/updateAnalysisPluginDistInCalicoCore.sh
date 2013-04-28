#!/bin/bash

CALICO_SERVER_HOME="/home/motta/workspace/irvine/Calico-Analysis/Server"
ant clean && ant dist 

cd $CALICO_SERVER_HOME
cp plugins/calico.server.analysis/dist/calico.server.analysis-trunk/calico.server.analysis.jar core/plugins/calico.server.analysis.jar
