cd /D "%~dp0"
cd MAVProxy
mavproxy --master=tcp:localhost:5760 --out=udp:localhost:14550 --out=udp:localhost:14560