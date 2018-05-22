 docker run  \
-v /var/run/docker.sock:/var/run/docker.sock \
     -v $(which docker):/usr/bin/docker:ro \
     -v /lib64/libdevmapper.so.1.02:/usr/lib/x86_64-linux-gnu/libdevmapper.so.1.02 \
     -v /lib64/libudev.so.0:/usr/lib/x86_64-linux-gnu/libudev.so.0 \
     -e JENKINS_USER=admin \
     -e JENKINS_PASS=admin \
     -p 8080:8080 \
     --name jenkins \
     --privileged=true -t -i \
davromalc/jenkins