from java.lang import Runtime

def main():
    global myarg0
    
    try:
        myarg0
    except NameError:
        print "Please input the class you want to open source file"
        return
    
    path = "/home/LiGeng/Desktop/Javasrc"
    
    s = myarg0.split(".")
    for x in s:
        path = ("%s/%s")%(path, x)
    path = ("%s.java") % path
    
    print path
    Runtime.getRuntime().exec(("emacs %s")%path)
    
main()