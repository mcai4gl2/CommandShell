def main(): 
    from shell import Shell
    Shell.getInstance().synchronize()
    usrCommands.update()
    print "Command update complete"
    
main()