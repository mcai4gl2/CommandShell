def main():
    from shell import Shell
    Shell.getInstance().synchronize()
    for x in [x for x in utilMap.keys()]:
        print x
        
main()