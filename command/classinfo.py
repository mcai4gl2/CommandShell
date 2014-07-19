from java.lang import Class
from java.lang.reflect import Modifier
import java.lang.ClassNotFoundException
from java.lang.reflect import Field
from java.lang.reflect import Method

def info(name):
    try:
        cls = Class.forName(name)
    except java.lang.ClassNotFoundException:
        print "Cannot found class: %s" % name
        return 
    
    print "="*20
    
    modifier = cls.getModifiers() 
    m = getModifiers(modifier)
    if cls.isInterface(): print "%s interface %s"%(m,name)
    else: print "%s class %s"%(m,name)
    
    fields = cls.getFields()
    print "-Fields"
    for field in fields:
        print ("--%s %s %s") % (getModifiers(field.getModifiers()),
                field.getType().getName(), field.getName())
    
    print
    
    methods = cls.getMethods()
    print "-Methods"
    for method in methods:
        print ("--%s %s %s(%s)") % (getModifiers(method.getModifiers()),
                method.getReturnType(), method.getName(), getParameters(method))
        exceptions = getExceptions(method)
        if not (exceptions == ""):
            print "----throws %s" % exceptions
    
    print
    
    print "-Superclass"
    if cls.getSuperclass():
        print cls.getSuperclass().getName()
    
    print "="*20
    
def getParameters(method):
    m = ""
    for x in method.getParameterTypes():
        m="%s %s" % (m, x.getName())
    return m
    
def getExceptions(method):
    m = ""
    for x in method.getExceptionTypes():
        m="%s %s" % (m, x.getName())
    return m

def getModifiers(modifier):    
    modifierCls = Class.forName("java.lang.reflect.Modifier")  
    modifiers = [k for k in dir(modifierCls) 
                 if k.startswith("is") and not k == "isInterface"]
    f = lambda x: getattr(modifierCls, x)
    g = map(f,modifiers)
    dic = zip(modifiers, g)
    m=""
    for x in dic:
        if x[1](modifier): 
            m="%s %s" % (m, x[0][2:].lower())
    return m        
    
def main():
    global myarg0, myarg1
    
    global myarg0
    try:
        myarg0
    except NameError:
        print "Please input the full name of a java class"
        myarg0="java.lang.Object"
    
    try:
        globals()['myarg1']
    except KeyError:
        pass
    else:
        print "Too many arguments. This command accepts one and only one argument"
        return
    
    info(myarg0)
    
main()