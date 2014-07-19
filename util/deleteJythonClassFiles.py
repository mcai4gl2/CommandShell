
def main():
    print "deleting py class files..."
    from java.io import File
    [x.delete() for x in File(commandDir).listFiles() 
         if x.getName().endswith("$py.class")]

main()