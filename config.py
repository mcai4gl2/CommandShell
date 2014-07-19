prompt=">>> "
welcomeString="Welcome to command shell"
exitString="See you"
commandDir="./command/"
utilDir="./util/"

debug="true"

if debug=="true": clearPyClassFiles="true"
else: clearPyClassFiles="false"

utilmapPath="./utilMap.xml"

#from shell import CommandList
#usrCommands=CommandList()
#utilCommands=CommandList()

import sys
sys.path.append("./pythonsrc")
import heap
usrCommands=heap.CommandHeap()
utilCommands=heap.CommandHeap()
