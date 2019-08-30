import sys

from opencog.type_constructors import *
from opencog.utilities import initialize_opencog
from opencog.scheme_wrapper import scheme_eval
import time


N = 3
file_create = sys.argv[1]
file_query = sys.argv[2]

print("create file:", file_create)
print("query file:", file_query)

def create_and_init_atomspace():
    atomspace = AtomSpace()
    initialize_opencog(atomspace)
    scheme_eval(atomspace, '(use-modules (opencog) (opencog exec))')
    return atomspace;

delta = 0

for i in range(N):
    atomspace = create_and_init_atomspace()
    t = time.time()
    scheme_eval(atomspace, '(load "%s")' % file_create)
    delta += (time.time() - t)

print("create time:", delta / N)

for atom in atomspace:
    if not atom.incoming:
        print(str(atom))

delta = 0

for i in range(N):
    t = time.time()
    res = scheme_eval(atomspace, '(load "%s")' % file_query)
    delta += (time.time() - t)

print("query time:", delta / N)

# print(res)
