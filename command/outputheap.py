from math import log

def outputHeap(heap,numOfblank=2):
    height=int(log(len(heap))/log(2)+1)
    firstLeaf=len(heap)/2-1
    printLength=1+(len(heap)-firstLeaf-1)*(numOfblank+1)
    
    for index in range(0, height-1):
        s=''
        for num in range(2**index,2**(index+1)):
            s+=str(heap[num-1])+numOfblank*" "
        print s.center(printLength)
    
    s=''
    for num in range(2**(height-1),len(heap)+1):
        s+=str(heap[num-1])+numOfblank*" "
    
    print s.center(printLength)

def main():
    global myarg0
    
    try:
        myarg0
    except NameError:
        print "Please input a list of numbers separated by comma"
        return
 
    outputHeap(myarg0.split(','))
    
main()