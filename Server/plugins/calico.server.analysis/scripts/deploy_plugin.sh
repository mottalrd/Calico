echo "**************** Start deploy ****************"
echo "**************** Building the plugin ****************"
export JAVA_HOME=/usr/lib/jvm/jdk1.6.0_32
ant clean
ant dist
CALICO_SERVER_HOME=/home/motta/Desktop/Calico/Calico-Analysis/Calico-Analysis-Server/
echo "**************** Copy into calico client plugin folder ****************"
cp dist/calico.server.analysis-trunk/calico.server.analysis.jar $CALICO_SERVER_HOME/calico3server/plugins/
echo "**************** Copy into calico client lib folder ****************"
cp dist/calico.server.analysis-trunk/calico.server.analysis.jar $CALICO_SERVER_HOME/calico3server/lib/
echo "**************** Deploy completed ****************"