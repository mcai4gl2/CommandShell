from java.lang import String

def main():
    f = lambda x: globals()[x]
    list = [x for x in globals().keys() if String(x).matches("^myarg\\d$")]
    for x in list:
        print x,"=",f(x)
main()