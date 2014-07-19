
def main():
    from shell import Shell
    Shell.getInstance().synchronize()
    for x in [x for x in usrCommands.getAllCommands()]:
        print x.split(".")[0]
        
main()