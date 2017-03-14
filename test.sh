killall -9 Xvfb
Xvfb :1999 -screen 0 1024x768x16 1>/dev/null 2>/dev/null &
DISPLAY=:1999 ant coverage
returnCode=$?

killall -9 Xvfb
exit $returnCode
