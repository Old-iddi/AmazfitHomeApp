# Amazfit HomeApp

A simple Android application for Amazfit Pace/Stratos smartwatches to control home
appliances via HTTP Switches over WiFi

Currently list of switches  must be set up in MainActivity.java in function getSwitches

each element in array contains [0] - title of switch, [1] - on URL, [2] - off URL

Also there is two buttons to Turn WiFi on and Turn Wifi OFF
No progress indicator for wifi buttons

light on/off buttons will be enbled after successfull connection to WiFi network
and disabled on disconnection