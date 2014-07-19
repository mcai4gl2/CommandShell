def main():
    global myarg0
    try:
        myarg0
    except NameError:
        print "Please input the command you want to run in Jython"
        return
    
    try:
        globals()['myarg1']
    except KeyError:
        pass
    else:
        print "Too many arguments. This command accepts one and only one argument"
        return
    
    if not (myarg0=="quit()"):
        exec(myarg0)
    
main()