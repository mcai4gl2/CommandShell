from shell import Commands
from java.io import File

"""
this class shows how jython class can implement Commands interface.
this class is a maxheap which provides no better performance.
for better performance, sort binary tree should be used.
"""

class CommandHeap(Commands):
    def __init__(self):
        pass
    
    def buildCommands(self,s):
        self.path=File(s)
        files=[]
        if self.path.isDirectory(): self.addCommands(self.path, files)
        self.heap=Heap(files)
    
    def addCommands(self, path, files):
        commands = path.listFiles()
        for x in commands:
            if x.isDirectory(): self.addCommands(x,files)
            else: files.append(x)
    
    def exists(self,command):
        return self.heap.exists(command)
    
    def update(self):
        self.buildCommands(self.path.getName())
    
    def getAllCommands(self):
        return self.heap.getAll()

class Heap:
    def __init__(self,list):
        self.parent=lambda index: index/2
        self.left=lambda index: 2*index
        self.right=lambda index: 2*index+1
        self.name=lambda x: x.getName()
        self.heap=list
        self.buildMaxHeap(self.heap)
        
    def buildMaxHeap(self, array):
        for i in range(len(array)/2,0):
            maxHeapify(array,len(array),i)
            
    def maxHeapify(self,heap,size, index):
        l=left(index)
        r=right(index)
        if l<=size and self.name(heap[l-1])>self.name(heap[index-1]): largest=l
        else:   largest=i
        if r<=size and self.name(heap[r-1])>self.name(heap[index-1]): largest=r
        if largest!=index:
                tmp=heap[index-1]
                heap[index-1]=heap[largest-1]
                heap[largest-1]=heap[index-1]
                maxHeapify(heap,size,largest)
    
    def exists(self, command):
        for x in self.heap:
            if self.name(x).split(".")[0] == command: return x
        return None
    
    def getAll(self):
        return map(self.name, self.heap)