#!/bin/bash

CALICO_CLIENT_HOME="/home/motta/workspace/irvine/Calico-Analysis/Client"
ant clean && ant dist 

cd $CALICO_CLIENT_HOME
cp plugins/calico.client.analysis/dist/calico.client.analysis-trunk/calico.client.analysis.jar core/plugins/calico.client.analysis.jar
