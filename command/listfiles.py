from java.io import File

"""
require arg0 as path
require arg1 as delimiter
require arg2 as flag indicating whether open sub-directories
"""

def printfiles(path, delimiter, level, flag):
    if path.isDirectory():
        print delimiter*level,path.getName(),"(D)"
        if flag=="y" or level == 0:
            try:
                for file in path.listFiles():
                    printfiles(file, delimiter, level+1, flag)
            except TypeError:
                print delimiter*level,"Access Denied"
    else:
        print delimiter*level,path.getName()

def main():
    
    global myarg0,myarg1,myarg2
    
    try:
        myarg0
    except NameError:
        print "please specify the path"
        return
    
    try:
        myarg1
    except NameError:
        myarg1 = "------"
    
    if myarg1 is not str or len(myarg1) <= 6: myarg1 = "------"
    
    try:
        myarg2
    except NameError:
        myarg2 = "n"
    
    f = File(myarg0)
    if (not f.isDirectory()) and (not f.isFile()):
        print "No such file or directory",myarg0
        return
    
    printfiles(f,myarg1,0,myarg2)
    
main()
