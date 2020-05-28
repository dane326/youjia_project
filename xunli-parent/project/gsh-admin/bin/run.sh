nohup java -Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -agentpath:linux/XunliByteCodeDecode.so -cp app/gsh-admin.jar com.gsh.GshApplication %cd% &
pause