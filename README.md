# HeartBeat-Mechanism
PyHeartBeat comprises two Java files: PyHBClient.java, which dispatches UDP packets, and PyHBServer.java, responsible for monitoring these packets and identifying inactive clients. The client application operates on multiple computers, transmitting UDP packets to a central server at regular intervals.

Within the server program, one module maintains an updated list of client IP addresses and their respective last received packet timestamps. Simultaneously, another module periodically scans this list to identify any clients inactive beyond a predetermined timeout duration.

This application doesn't necessitate the use of reliable TCP connections since sporadic packet loss doesn't trigger false alarms, provided the server's timeout exceeds the client's transmission interval. However, when monitoring numerous computers, it's advisable to minimize bandwidth and server load by opting for small UDP packets over TCP connections.

Clients dispatch packets every 10 seconds, while the server conducts checks every 30 seconds, aligning the timeout period accordingly. These configurations, including the server's IP address and port, are adjustable based on specific requirements.

Furthermore, debug printouts can be deactivated within the Java code or via command-line options such as -q for quiet mode and -v for verbose mode.
